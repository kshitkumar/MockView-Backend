package com.hashedin.mockview.service;

import com.hashedin.mockview.dto.InterviewerDto;
import com.hashedin.mockview.dto.MyInterviewDto;
import com.hashedin.mockview.dto.TimeSlot;
import com.hashedin.mockview.exception.BadRequestException;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.Slot;
import com.hashedin.mockview.model.SlotStatus;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.model.UserWorkExperience;
import com.hashedin.mockview.repository.SlotRepository;
import com.hashedin.mockview.repository.UserRepository;
import com.hashedin.mockview.repository.UserWorkExperienceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InterviewService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SlotRepository slotRepository;
    @Autowired
    UserWorkExperienceRepository userWorkExperienceRepository;

    @Autowired
    ExperienceService experienceService;


    private final java.sql.Date currentDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());


    public List<MyInterviewDto> getUpcomingInterviewForInterviewer(Integer id) throws ResourceNotFoundException {


        User loggedInUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));
        List<Slot> slotList = slotRepository.findByInterviewerAndSlotStatusAndInterviewDateGreaterThanEqual(loggedInUser, SlotStatus.BOOKED, currentDate);
        log.debug("Data returned by query is {}", slotList);

        Set idList = slotList.stream()
                .map(x -> x.getInterviewee().getId())
                .collect(Collectors.toSet());

        List<User> userList = userRepository.findByIdIn(idList);
        List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository.findByUserIn(userList);


        Map<User, List<UserWorkExperience>> userWorkExperienceMap = userWorkExperienceList.stream()
                .collect(Collectors.groupingBy(UserWorkExperience::getUser));

        HashMap<User, UserWorkExperience> currentCompanyMap = new HashMap<>();

        //populating currentCompany

        for (Map.Entry<User, List<UserWorkExperience>> entry : userWorkExperienceMap.entrySet()) {
            User user = entry.getKey();
            List<UserWorkExperience> workExperienceList = entry.getValue();
            currentCompanyMap.put(user, experienceService.getCurrentCompany(workExperienceList));

        }

        List<MyInterviewDto> myInterviewDtoList = new ArrayList<>();


        for (Slot slot : slotList) {
            MyInterviewDto myInterviewDto = new MyInterviewDto();

            myInterviewDto.setId(slot.getId());
            myInterviewDto.setName(slot.getInterviewee().getFirstName() + " " + slot.getInterviewee().getLastName());
            myInterviewDto.setCompany(currentCompanyMap.get(slot.getInterviewee()).getCompanyName());
            myInterviewDto.setPosition(currentCompanyMap.get(slot.getInterviewee()).getPosition());
            myInterviewDto.setStartDate(slot.getInterviewDate());
            myInterviewDto.setStartTime(slot.getInterviewStartTime());
            myInterviewDto.setEndTime(slot.getInterviewStartTime().plusHours(1));
            myInterviewDto.setAmount(slot.getInterviewCharges());


            myInterviewDtoList.add(myInterviewDto);

        }
        return myInterviewDtoList;


    }

    public List<MyInterviewDto> getUpcomingInterviewForInterviewee(Integer id) throws ResourceNotFoundException {

        User loggedInUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));
        List<Slot> slotList = slotRepository.findByIntervieweeAndSlotStatusAndInterviewDateGreaterThanEqual(loggedInUser, SlotStatus.BOOKED, currentDate);
        log.debug("Data returned by query is {}", slotList);

        Set idList = slotList.stream()
                .map(x -> x.getInterviewer().getId())
                .collect(Collectors.toSet());

        List<User> userList = userRepository.findByIdIn(idList);
        List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository.findByUserIn(userList);


        Map<User, List<UserWorkExperience>> userWorkExperienceMap = userWorkExperienceList.stream()
                .collect(Collectors.groupingBy(UserWorkExperience::getUser));

        HashMap<User, UserWorkExperience> currentCompanyMap = new HashMap<>();

        //populating currentCompany

        for (Map.Entry<User, List<UserWorkExperience>> entry : userWorkExperienceMap.entrySet()) {
            User user = entry.getKey();
            List<UserWorkExperience> workExperienceList = entry.getValue();
            currentCompanyMap.put(user, experienceService.getCurrentCompany(workExperienceList));

        }

        List<MyInterviewDto> myInterviewDtoList = new ArrayList<>();


        for (Slot slot : slotList) {
            MyInterviewDto myInterviewDto = new MyInterviewDto();

            myInterviewDto.setId(slot.getId());
            myInterviewDto.setName(slot.getInterviewer().getFirstName() + " " + slot.getInterviewer().getLastName());
            myInterviewDto.setCompany(currentCompanyMap.get(slot.getInterviewer()).getCompanyName());
            myInterviewDto.setPosition(currentCompanyMap.get(slot.getInterviewer()).getPosition());
            myInterviewDto.setStartDate(slot.getInterviewDate());
            myInterviewDto.setStartTime(slot.getInterviewStartTime());
            myInterviewDto.setEndTime(slot.getInterviewStartTime().plusHours(1));
            myInterviewDto.setAmount(slot.getInterviewCharges());


            myInterviewDtoList.add(myInterviewDto);

        }
        return myInterviewDtoList;


    }

    public List<MyInterviewDto> getCompletedInterviewForInterviewer(Integer id) throws ResourceNotFoundException {
        User loggedInUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));
        List<SlotStatus> slotStatusList = Arrays.asList(SlotStatus.COMPLETED,SlotStatus.BOOKED);
        List<Slot> slotListCompleted = slotRepository.findByInterviewerAndSlotStatusInAndInterviewDateLessThan(loggedInUser, slotStatusList, currentDate);

        // currently not handled time logic

        // making all previous slots less than current date as completed
        slotListCompleted.stream().forEach(x ->
                {
                    x.setSlotStatus(SlotStatus.COMPLETED);
                }
        );

        slotRepository.saveAll(slotListCompleted);


        log.debug("Data returned by query is {}", slotListCompleted);

        Set idList = slotListCompleted.stream()
                .map(x -> x.getInterviewee().getId())
                .collect(Collectors.toSet());

        List<User> userList = userRepository.findByIdIn(idList);
        List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository.findByUserIn(userList);


        Map<User, List<UserWorkExperience>> userWorkExperienceMap = userWorkExperienceList.stream()
                .collect(Collectors.groupingBy(UserWorkExperience::getUser));

        HashMap<User, UserWorkExperience> currentCompanyMap = new HashMap<>();

        //populating currentCompany

        for (Map.Entry<User, List<UserWorkExperience>> entry : userWorkExperienceMap.entrySet()) {
            User user = entry.getKey();
            List<UserWorkExperience> workExperienceList = entry.getValue();
            currentCompanyMap.put(user, experienceService.getCurrentCompany(workExperienceList));

        }

        List<MyInterviewDto> myInterviewDtoList = new ArrayList<>();


        for (Slot slot : slotListCompleted) {

            MyInterviewDto myInterviewDto = new MyInterviewDto();

            myInterviewDto.setId(slot.getId());
            myInterviewDto.setName(slot.getInterviewee().getFirstName() + " " + slot.getInterviewee().getLastName());
            myInterviewDto.setCompany(currentCompanyMap.get(slot.getInterviewee()).getCompanyName());
            myInterviewDto.setPosition(currentCompanyMap.get(slot.getInterviewee()).getPosition());
            myInterviewDto.setStartDate(slot.getInterviewDate());
            myInterviewDto.setStartTime(slot.getInterviewStartTime());
            myInterviewDto.setEndTime(slot.getInterviewStartTime().plusHours(1));
            myInterviewDto.setAmount(slot.getInterviewCharges());


            myInterviewDtoList.add(myInterviewDto);

        }
        return myInterviewDtoList;


    }

    public List<MyInterviewDto> getCompletedInterviewForInterviewee(Integer id) throws ResourceNotFoundException {

        User loggedInUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));
        List<SlotStatus> slotStatusList = Arrays.asList(SlotStatus.COMPLETED,SlotStatus.BOOKED);
        List<Slot> slotListCompleted = slotRepository.findByIntervieweeAndSlotStatusInAndInterviewDateLessThan(loggedInUser, slotStatusList, currentDate);

        // currently not handled time logic

        // making all previous slots less than current date as completed
        slotListCompleted.stream().forEach(x ->
                {
                    x.setSlotStatus(SlotStatus.COMPLETED);
                }
        );

        slotRepository.saveAll(slotListCompleted);


        log.debug("Data returned by query is {}", slotListCompleted);

        Set idList = slotListCompleted.stream()
                .map(x -> x.getInterviewer().getId())
                .collect(Collectors.toSet());

        List<User> userList = userRepository.findByIdIn(idList);
        List<UserWorkExperience> userWorkExperienceList = userWorkExperienceRepository.findByUserIn(userList);


        Map<User, List<UserWorkExperience>> userWorkExperienceMap = userWorkExperienceList.stream()
                .collect(Collectors.groupingBy(UserWorkExperience::getUser));

        HashMap<User, UserWorkExperience> currentCompanyMap = new HashMap<>();

        //populating currentCompany

        for (Map.Entry<User, List<UserWorkExperience>> entry : userWorkExperienceMap.entrySet()) {
            User user = entry.getKey();
            List<UserWorkExperience> workExperienceList = entry.getValue();
            currentCompanyMap.put(user, experienceService.getCurrentCompany(workExperienceList));

        }

        List<MyInterviewDto> myInterviewDtoList = new ArrayList<>();


        for (Slot slot : slotListCompleted) {

            MyInterviewDto myInterviewDto = new MyInterviewDto();

            myInterviewDto.setId(slot.getId());
            myInterviewDto.setName(slot.getInterviewer().getFirstName() + " " + slot.getInterviewer().getLastName());
            myInterviewDto.setCompany(currentCompanyMap.get(slot.getInterviewer()).getCompanyName());
            myInterviewDto.setPosition(currentCompanyMap.get(slot.getInterviewer()).getPosition());
            myInterviewDto.setStartDate(slot.getInterviewDate());
            myInterviewDto.setStartTime(slot.getInterviewStartTime());
            myInterviewDto.setEndTime(slot.getInterviewStartTime().plusHours(1));
            myInterviewDto.setAmount(slot.getInterviewCharges());


            myInterviewDtoList.add(myInterviewDto);

        }
        return myInterviewDtoList;


    }
}
