package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.InterviewerDto;
import com.hashedin.mockview.dto.SlotDto;
import com.hashedin.mockview.dto.TimeSlot;
import com.hashedin.mockview.exception.BadRequestException;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.*;
import com.hashedin.mockview.repository.SlotRepository;
import com.hashedin.mockview.repository.UserRepository;
import com.hashedin.mockview.repository.UserWorkExperienceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalTime;
import java.util.*;
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

    public void setSlotsForAvailability(Integer id, SlotDto slotDto) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));
        List<Slot> slotList = slotDto.getSlotList();
        Integer interviewCharges = slotDto.getInterviewCharges();

        List<Slot> slotsToBeDeleted = getSlotsForCurrentUserMoreThanCurrentDate(user);

        List<Integer> slotsIdToBeDeleted = slotsToBeDeleted.stream()
                .filter(x ->x.getSlotStatus() == SlotStatus.VACANT)
                .map(x ->x.getId()).collect(Collectors.toList());

        slotRepository.deleteByIdIn(slotsIdToBeDeleted);


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
    private List<Slot> getSlotsForCurrentUserMoreThanCurrentDate(User user)
    {
        java.sql.Date currentDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());

        List<Slot> slotList =new ArrayList<>();

        List<Slot> slotListVacant = slotRepository.findByInterviewerAndInterviewDateGreaterThanAndSlotStatus(user, currentDate, SlotStatus.VACANT);
        List<Slot> slotListBooked =slotRepository.findByInterviewerAndInterviewDateGreaterThanAndSlotStatus(user,currentDate,SlotStatus.BOOKED);

        slotList.addAll(slotListBooked);
        slotList.addAll(slotListVacant);

        return slotList;
    }


    public List<InterviewerDto> findInterviewers(Integer id, Industry industry, Date date, String company, Position position, LocalTime startTime, LocalTime endTime) throws BadRequestException, ResourceNotFoundException {

        // getting data from database
        User loggedInUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));


        List<Slot> slotsAvailable = slotRepository.findByInterviewDateAndSlotStatus(date, SlotStatus.VACANT);

        Set idList = slotsAvailable.stream()
                .map(x -> x.getInterviewer().getId())
                .collect(Collectors.toSet());

        List<User> userList = userRepository.findByIdIn(idList);

        List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository.findByUserIn(userList);


        // creating Map for certain fields

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
                .filter(x -> ExperienceService.filterForIndustry(industry, x))
                .filter(x -> ExperienceService.filterForCompany(company, x))
                .filter(x -> ExperienceService.filterForPosition(position, x))
                .collect(Collectors.toList());

        List<Slot> filteredSlotList = slotsAvailable.stream()
                .filter(x -> SlotService.filterForTime(startTime, endTime, x))
                .collect(Collectors.toList());

        // creating Set and Map for filtered Data

        Set<Integer> filteredExperienceSet = filteredExperienceList.stream()
                .map(x -> x.getUser().getId())
                .collect(Collectors.toSet());


        Map<User, List<Slot>> filterSlotMap = filteredSlotList.stream()
                .collect(Collectors.groupingBy(Slot::getInterviewer));

        // based on both criteria updating user in list

        List<User> filteredUserList = userList.stream()
                .filter(x -> filterSlotMap.containsKey(x) && filteredExperienceSet.contains(x.getId()) && x.getId() != loggedInUser.getId())
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
            interviewerDto.setInterviewerName(u.getFirstName() + " " + u.getLastName());
            interviewerDto.setId(u.getId());
            List<Slot> slotsForCurrentInterviewer = filterSlotMap.get(u);
            List<TimeSlot> slotList = new ArrayList<>();
            for (Slot slots : slotsForCurrentInterviewer) {
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setId(slots.getId());
                timeSlot.setStartTime(slots.getInterviewStartTime());
                timeSlot.setEndTime(slots.getInterviewStartTime().plusHours(1));

                slotList.add(timeSlot);

            }
            interviewerDto.setTimeSlots(slotList);

            interviewerDtoList.add(interviewerDto);

        }
        if (interviewerDtoList.isEmpty())
            throw new BadRequestException("No Interviewer Found for given criteria");
        else
            return interviewerDtoList;


    }


    public List<TimeSlot> getSlotsForAvailability(Integer id) throws ResourceNotFoundException {
        User loggedInUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));

        List<Slot> slotList = getSlotsForCurrentUserMoreThanCurrentDate(loggedInUser);

        List<TimeSlot> timeSlotList = new ArrayList<>();

        for (Slot slot : slotList) {
            TimeSlot timeSlot = TimeSlot.builder()
                    .id(slot.getId())
                    .startTime(slot.getInterviewStartTime())
                    .date(slot.getInterviewDate())
                    .slotStatus(slot.getSlotStatus())
                    .build();
            timeSlotList.add(timeSlot);
        }
        return timeSlotList;


    }

    public void bookInterviewSlotForUser(Integer id, Integer slotId) throws ResourceNotFoundException {
        User loggedInUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));
        Slot slotToBeBooked =slotRepository.findById(slotId)
                .orElseThrow(() ->new ResourceNotFoundException("No Slot Found associated with id: "+ slotId));

//        slotToBeBooked.setSlotStatus(SlotStatus.BOOKED);
//        slotToBeBooked.setInterviewee(loggedInUser);

        slotRepository.updateIntervieweeAndStatus(slotToBeBooked.getId(),loggedInUser,SlotStatus.BOOKED);

    }
}
