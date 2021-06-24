package com.hashedin.mockview.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hashedin.mockview.dto.InterviewerDto;
import com.hashedin.mockview.dto.SlotDto;
import com.hashedin.mockview.exception.BadRequestException;
import com.hashedin.mockview.exception.ResourceNotFoundException;
import com.hashedin.mockview.model.Industry;
import com.hashedin.mockview.model.Position;
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

    @PostMapping("{userId}/availability")
    public ResponseEntity<Void> bookSlotForInterview(@PathVariable Integer userId,
                                                     @RequestBody SlotDto slotDto) throws ResourceNotFoundException {
        slotService.bookSlots(userId, slotDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/interviewers")
    public ResponseEntity<List<InterviewerDto>> getInterviewers(@RequestParam("industry") Industry industry
            , @RequestParam(value = "date") @JsonFormat(pattern = "yyyy-MM-dd") Date date
            , @RequestParam(value = "startTime", required = false) String startTime
            , @RequestParam(value = "endTime", required = false) String endTime
            , @RequestParam(value = "company", required = false) String company
            , @RequestParam(value = "position", required = false) Position position) throws BadRequestException {


        LocalTime convertedStartTime = null;
        LocalTime convertedEndTime = null;
        if (startTime != null && endTime != null) {
            convertedStartTime = LocalTime.parse(startTime);
            convertedEndTime = LocalTime.parse(endTime);
        }

        List<InterviewerDto> interviewerDtoList = slotService.findInterviewers(industry, date, company, position, convertedStartTime, convertedEndTime);


        return new ResponseEntity<>(interviewerDtoList, HttpStatus.OK);

    }

}
