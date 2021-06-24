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
import java.util.*;
import java.util.function.Function;
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
    @Autowired
    ExperienceService experienceService;

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

    public static boolean filterForTime(LocalTime startTime, LocalTime endTime, Slot slot) {
        if (startTime != null && endTime != null) {
            if (slot.getInterviewStartTime().compareTo(startTime) >= 0 &&
                    slot.getInterviewStartTime().compareTo(endTime) < 0)
                return true;
            else
                return false;
        }
        return true;
    }

    public static boolean filterForIndustry(Industry industry, UserWorkExperience userWorkExperience) {
        if (industry != null) {
            if (userWorkExperience.getIndustry().equals(industry))
                return true;
            else
                return false;
        }
        return true;
    }

    public static boolean filterForCompany(String company, UserWorkExperience userWorkExperience) {
        if (company != null) {
            if (userWorkExperience.getCompanyName().equals(company))
                return true;
            else
                return false;
        }
        return true;
    }

    public static boolean filterForPosition(Position position, UserWorkExperience userWorkExperience) {
        if (position != null) {
            if (userWorkExperience.getPosition().equals(position))
                return true;
            else
                return false;
        }
        return true;
    }


    public List<InterviewerDto> findInterviewers(Industry industry, Date date, String company, Position position, LocalTime startTime, LocalTime endTime) throws ResourceNotFoundException {

        // getting data from database

        List<Slot> slotsAvailable = slotRepository.findByInterviewDateAndSlotStatus(date, SlotStatus.VACANT);

        Set idList = slotsAvailable.stream()
                .map(x -> x.getInterviewer().getId())
                .collect(Collectors.toSet());

        List<User> userList = userRepository.findByIdIn(idList);

        List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository.findByUserIn(userList);


        // creating Map for certain fields

//        Map<User, List<Slot>> slotMap = slotsAvailable.stream()
//                .collect(Collectors.groupingBy(Slot::getInterviewer));
//
//        Map<Integer, User> userMap = userList.stream()
//                .collect(Collectors.toMap(User::getId, User -> User));

        Map<User, List<UserWorkExperience>> userWorkExperienceMap = userWorkExperienceList.stream()
                .collect(Collectors.groupingBy(UserWorkExperience::getUser));

        Map<User, Double> experienceMap = new HashMap<>();

        HashMap<User, UserWorkExperience> currentCompanyMap = new HashMap<>();

        // handling logic of Current Company and Experience

        for (Map.Entry<User, List<UserWorkExperience>> entry : userWorkExperienceMap.entrySet()) {
            User user = entry.getKey();
            List<UserWorkExperience> workExperienceList = entry.getValue();
            Double experienceInYears = experienceService.calculateUserExperience(workExperienceList);

            currentCompanyMap.put(user, experienceService.getCurrentCompany(workExperienceList));

            experienceMap.put(user, experienceInYears);
        }


        // filtering criteria

        List<UserWorkExperience> filteredExperienceList = userWorkExperienceList.stream()
                .filter(x -> SlotService.filterForIndustry(industry, x))
                .filter(x -> SlotService.filterForCompany(company, x))
                .filter(x -> SlotService.filterForPosition(position, x))
                .collect(Collectors.toList());

        List<Slot> filteredSlotList = slotsAvailable.stream()
                .filter(x -> SlotService.filterForTime(startTime, endTime, x))
                .collect(Collectors.toList());

        // creating Set and Map fro filtered Data

        Set<Integer> filteredExperienceSet = filteredExperienceList.stream()
                .map(x -> x.getUser().getId())
                .collect(Collectors.toSet());


        Map<User, List<Slot>> filterSlotMap = filteredSlotList.stream()
                .collect(Collectors.groupingBy(Slot::getInterviewer));

        // based on both criteria updating user in list

        List<User> filteredUserList = userList.stream()
                .filter(x -> filterSlotMap.containsKey(x) && filteredExperienceSet.contains(x.getId()))
                .collect(Collectors.toList());


        // populating response Data
        List<InterviewerDto> interviewerDtoList = new ArrayList<>();


        for (User u : filteredUserList) {
            InterviewerDto interviewerDto = new InterviewerDto();

            interviewerDto.setExperience(experienceMap.get(u));
            interviewerDto.setPosition(currentCompanyMap.get(u).getPosition());
            interviewerDto.setEndingDate(currentCompanyMap.get(u).getEndingDate());
            interviewerDto.setJoiningDate(currentCompanyMap.get(u).getJoiningDate());
            interviewerDto.setCompany(currentCompanyMap.get(u).getCompanyName());
            interviewerDto.setInterviewerName(u.getFirstName() + " "+ u.getLastName());

            List<Slot> slotsForCurrentInterviewer = filterSlotMap.get(u);
            List<TimeSlot> slotList = new ArrayList<>();
            for (Slot slots : slotsForCurrentInterviewer) {
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setStartTime(slots.getInterviewStartTime());
                timeSlot.setEndTime(slots.getInterviewStartTime().plusHours(1));

                slotList.add(timeSlot);

            }
            interviewerDto.setTimeSlots(slotList);

            interviewerDtoList.add(interviewerDto);

        }
        return interviewerDtoList;


    }


}
