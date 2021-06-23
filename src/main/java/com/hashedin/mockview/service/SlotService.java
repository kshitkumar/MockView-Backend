package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.InterviewerDto;
import com.hashedin.mockview.dto.SlotDto;
import com.hashedin.mockview.dto.TimeSlot;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.*;
import com.hashedin.mockview.repository.SlotRepository;
import com.hashedin.mockview.repository.UserRepository;
import com.hashedin.mockview.repository.UserWorkExperienceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SlotService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    UserWorkExperienceRepository userWorkExperienceRepository;

    public void bookSlots(Integer id, SlotDto slotDto) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));
        List<Slot> slotList = slotDto.getSlotList();
        Integer interviewCharges = slotDto.getInterviewCharges();

        slotList.stream().forEach(item ->
        {
            item.setInterviewer(user);
            item.setSlotStatus(SlotStatus.VACANT);
            item.setInterviewCharges(interviewCharges);
        });
        slotRepository.saveAll(slotList);
    }

    public List<InterviewerDto> findInterviewers(Integer id, Industry industry, Date date, String company, Position position, LocalTime startTime, LocalTime endTime) throws ResourceNotFoundException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));

        List<InterviewerDto> interviewerDtoList = new ArrayList<>();
        // all null
        if (company == null && position == null && (startTime == null && endTime == null)) {
            List<Slot> slotsAvailable = slotRepository.findByInterviewDateAndSlotStatus(date, SlotStatus.VACANT);

            List<Integer> idList = slotsAvailable.stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<User> userList = userRepository.findByIdIn(idList);
            List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository.findByIndustryAndUserIn(industry, userList);

            log.debug("User details when all optional fields are null are {}", userWorkExperienceList);




            for (User u : userList) {
                InterviewerDto interviewerDto = new InterviewerDto();
                List<UserWorkExperience> workExperienceListForParticularId = userWorkExperienceList.parallelStream().filter(x -> x.getUser().equals(u)).collect(Collectors.toList());

                Double totalExperience = 0.0;
                for (UserWorkExperience experience : workExperienceListForParticularId) {
                    //not current employment
                    if (experience.getEndingDate() != null) {
                        int m1 = experience.getJoiningDate().getYear() * 12 + experience.getJoiningDate().getMonth();

                        int m2 = experience.getEndingDate().getYear() * 12 + experience.getEndingDate().getMonth();
                        totalExperience += m2 - m1 + 1;
                    }
                    //is current employment
                    else {
                        interviewerDto.setCompany(experience.getCompanyName());
                        interviewerDto.setPosition(experience.getPosition());
                        interviewerDto.setEndingDate(null);
                        interviewerDto.setJoiningDate(experience.getJoiningDate());

                        //Experience calculation Logic

                        java.util.Date currentDate = new java.util.Date();

                        int m1 = experience.getJoiningDate().getYear() * 12 + experience.getJoiningDate().getMonth();
                        int m2 = currentDate.getYear() * 12 + currentDate.getMonth();
                        totalExperience += m2 - m1 + 1;

                    }
                }
                totalExperience /= 12;
                interviewerDto.setExperience(totalExperience);
               List<Slot> slotsForCurrentInterviewer=  slotsAvailable.parallelStream().filter(x ->x.getInterviewer().equals(u)).collect(Collectors.toList());
               Integer slotCharges;
               List<TimeSlot> slotList =new ArrayList<>();
               for(Slot slots :slotsForCurrentInterviewer)
                {
                    TimeSlot timeSlot =new TimeSlot();
                    timeSlot.setStartTime(slots.getInterviewStartTime());
                    timeSlot.setEndTime(slots.getInterviewStartTime().plusHours(1));

                    slotList.add(timeSlot);

                }
               interviewerDto.setTimeSlots(slotList);

               interviewerDtoList.add(interviewerDto);

            }
            return interviewerDtoList;


        }
        // position not null
        else if (company == null && (startTime == null && endTime == null)) {
            List<Slot> slotsAvailable = slotRepository.findByInterviewDateAndSlotStatus(date, SlotStatus.VACANT);
            List<Integer> idList = slotsAvailable.stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<User> userList = userRepository.findByIdIn(idList);
            List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository
                    .findByIndustryAndPositionAndUserIn(industry, position, userList);
            log.debug("User details when Position is not null are {}", userWorkExperienceList);


        }
        // company not null
        else if (position == null && (startTime == null && endTime == null)) {
            List<Slot> slotsAvailable = slotRepository.findByInterviewDateAndSlotStatus(date, SlotStatus.VACANT);
            List<Integer> idList = slotsAvailable.stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<User> userList = userRepository.findByIdIn(idList);
            List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository
                    .findByIndustryAndCompanyNameAndUserIn(industry, company, userList);
            log.debug("User details when Position is not null are {}", userWorkExperienceList);
        }
        // time not null
        else if (position == null && company == null) {
            List<Slot> slotsAvailable = slotRepository
                    .findByInterviewDateAndSlotStatusAndInterviewStartTimeGreaterThanEqualAndInterviewStartTimeLessThan(date, SlotStatus.VACANT, startTime, endTime);
            List<Integer> idList = slotsAvailable.stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<User> userList = userRepository.findByIdIn(idList);
            List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository
                    .findByIndustryAndUserIn(industry, userList);

            log.debug("User details when Position is not null are {}", userWorkExperienceList);

        }
        //company is null
        else if (company == null) {
            List<Slot> slotsAvailable = slotRepository
                    .findByInterviewDateAndSlotStatusAndInterviewStartTimeGreaterThanEqualAndInterviewStartTimeLessThan(date, SlotStatus.VACANT, startTime, endTime);

            List<Integer> idList = slotsAvailable.stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<User> userList = userRepository.findByIdIn(idList);
            List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository
                    .findByIndustryAndPositionAndUserIn(industry, position, userList);
            log.debug("User details when company is null are {}", userWorkExperienceList);
        }
        //position is null
        else if (position == null) {
            List<Slot> slotsAvailable = slotRepository
                    .findByInterviewDateAndSlotStatusAndInterviewStartTimeGreaterThanEqualAndInterviewStartTimeLessThan(date, SlotStatus.VACANT, startTime, endTime);

            List<Integer> idList = slotsAvailable.stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<User> userList = userRepository.findByIdIn(idList);
            List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository
                    .findByIndustryAndCompanyNameAndUserIn(industry, company, userList);
            log.debug("User details when position is null are {}", userWorkExperienceList);
        }
        //time is null
        else if ((startTime == null && endTime == null)) {
            List<Slot> slotsAvailable = slotRepository.findByInterviewDateAndSlotStatus(date, SlotStatus.VACANT);

            List<Integer> idList = slotsAvailable.stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<User> userList = userRepository.findByIdIn(idList);
            List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository
                    .findByIndustryAndCompanyNameAndPositionAndUserIn(industry, company, position, userList);
            log.debug("User details when time is null are {}", userWorkExperienceList);

        }
        //nothing is null
        else {
            List<Slot> slotsAvailable = slotRepository.findByInterviewDateAndSlotStatus(date, SlotStatus.VACANT);

            List<Integer> idList = slotsAvailable.stream()
                    .map(x -> x.getId())
                    .collect(Collectors.toList());
            List<User> userList = userRepository.findByIdIn(idList);
            List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository
                    .findByIndustryAndCompanyNameAndPositionAndUserIn(industry, company, position, userList);
            log.debug("User details when nothing is null are {}", userWorkExperienceList);
        }
        return  new ArrayList<>();


    }
}
