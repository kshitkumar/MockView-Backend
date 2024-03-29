package com.hashedin.mockview.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hashedin.mockview.dto.InterviewerDto;
import com.hashedin.mockview.dto.MyInterviewDto;
import com.hashedin.mockview.dto.SlotDto;
import com.hashedin.mockview.dto.TimeSlot;
import com.hashedin.mockview.exception.BadRequestException;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.Industry;
import com.hashedin.mockview.model.Position;
import com.hashedin.mockview.service.InterviewService;
import com.hashedin.mockview.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/interviews")
public class InterviewController {


    @Autowired
    SlotService slotService;
    @Autowired
    InterviewService interviewService;

    @PostMapping("/{userId}/availability")
    public ResponseEntity<Void> setSlotsForAvailability(@PathVariable Integer userId,
                                                        @RequestBody SlotDto slotDto) throws ResourceNotFoundException {
        slotService.setSlotsForAvailability(userId, slotDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/availability")
    public ResponseEntity<List<TimeSlot>> getSlotsOfAvailability(@PathVariable Integer userId) throws ResourceNotFoundException {
        List<TimeSlot> slotDtoList = slotService.getSlotsForAvailability(userId);
        return new ResponseEntity<>(slotDtoList, HttpStatus.OK);
    }

    //upcoming interviews for interviewer
    @GetMapping("/{userId}/interviewer/upcoming")
    public ResponseEntity<List<MyInterviewDto>> getUpcomingInterviewForInterviewer(@PathVariable Integer userId) throws ResourceNotFoundException {
        List<MyInterviewDto> myInterviewDtoList = interviewService.getUpcomingInterviewForInterviewer(userId);
        return new ResponseEntity<>(myInterviewDtoList, HttpStatus.OK);
    }

    //upcoming interviews for interviewee
    @GetMapping("/{userId}/interviewee/upcoming")
    public ResponseEntity<List<MyInterviewDto>> getUpcomingInterviewForInterviewee(@PathVariable Integer userId) throws ResourceNotFoundException {
        List<MyInterviewDto> myInterviewDtoList = interviewService.getUpcomingInterviewForInterviewee(userId);
        return new ResponseEntity<>(myInterviewDtoList, HttpStatus.OK);
    }

    //Past Interviews for Interviewer
    @GetMapping("/{userId}/interviewer/completed")
    public ResponseEntity<List<MyInterviewDto>> getCompletedInterviewForInterviewer(@PathVariable Integer userId) throws ResourceNotFoundException {
        List<MyInterviewDto> myInterviewDtoList = interviewService.getCompletedInterviewForInterviewer(userId);
        return new ResponseEntity<>(myInterviewDtoList, HttpStatus.OK);
    }

    //Past Interviews for Interviewee
    @GetMapping("/{userId}/interviewee/completed")
    public ResponseEntity<List<MyInterviewDto>> getCompletedInterviewForInterviewee(@PathVariable Integer userId) throws ResourceNotFoundException {
        List<MyInterviewDto> myInterviewDtoList = interviewService.getCompletedInterviewForInterviewee(userId);
        return new ResponseEntity<>(myInterviewDtoList, HttpStatus.OK);
    }

    @PostMapping("/{userId}/slots/{slotId}")
    public ResponseEntity<Void> bookInterviewSlotForUser(@PathVariable("userId") Integer userId,
                                                         @PathVariable("slotId") Integer slotId) throws ResourceNotFoundException {
        slotService.bookInterviewSlotForUser(userId, slotId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}/interviewers")
    public ResponseEntity<List<InterviewerDto>> getInterviewers(@PathVariable Integer userId,
                                                                @RequestParam("industry") Industry industry
            , @RequestParam(value = "date") @JsonFormat(pattern = "yyyy-MM-dd") Date date
            , @RequestParam(value = "startTime", required = false) String startTime
            , @RequestParam(value = "endTime", required = false) String endTime
            , @RequestParam(value = "company", required = false) String company
            , @RequestParam(value = "position", required = false) Position position) throws BadRequestException, ResourceNotFoundException {


        LocalTime convertedStartTime = null;
        LocalTime convertedEndTime = null;
        if (startTime != null && endTime != null && !startTime.equals("") && !endTime.equals("")) {
            convertedStartTime = LocalTime.parse(startTime);
            convertedEndTime = LocalTime.parse(endTime);
        }

        if (company == null || company.equals(""))
            company = null;

        List<InterviewerDto> interviewerDtoList = slotService.findInterviewers(userId, industry, date, company, position, convertedStartTime, convertedEndTime);



        return new ResponseEntity<>(interviewerDtoList, HttpStatus.OK);

    }

}
