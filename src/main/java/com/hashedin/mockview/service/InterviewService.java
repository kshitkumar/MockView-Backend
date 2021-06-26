package com.hashedin.mockview.service;

import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.Slot;
import com.hashedin.mockview.model.SlotStatus;
import com.hashedin.mockview.model.User;
import com.hashedin.mockview.repository.SlotRepository;
import com.hashedin.mockview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

@Service
public class InterviewService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SlotRepository slotRepository;

    private final java.sql.Date currentDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());


    private LocalTime getCurrentTimeIST()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        ZoneId zone1 = ZoneId.of("Asia/Kolkata");
        LocalTime time1 = LocalTime.now(zone1);
        return LocalTime.parse(formatter.format(time1));
    }
    public void getUpcomingInterviewForInterviewer(Integer id) throws ResourceNotFoundException {

      LocalTime currentTime =getCurrentTimeIST();

        User loggedInUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found for id : " + id));
//        List<Slot> slotList = slotRepository.findByInterviewerAndSlotStatusAndDateGreaterThanEqualAndInterviewStartTimeGreaterThanEqual(loggedInUser, SlotStatus.BOOKED,currentTime);


    }
}
