package com.hashedin.mockview.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hashedin.mockview.dto.InterviewerDto;
import com.hashedin.mockview.dto.SlotDto;
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
@RequestMapping("/interviews")
public class InterviewController {


    @Autowired
    SlotService slotService;

    @PostMapping("{userId}/availability")
    public ResponseEntity<Void> bookSlotForInterview(@PathVariable Integer userId,
                                                     @RequestBody SlotDto slotDto) throws ResourceNotFoundException {
        slotService.bookSlots(userId, slotDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //TODO :Add timing wise filtering also

    @GetMapping("/interviewers")
    public ResponseEntity<List<InterviewerDto>> getInterviewers(@RequestParam("industry") Industry industry
            , @RequestParam(value = "date") @JsonFormat(pattern = "yyyy-MM-dd") Date date
            , @RequestParam(value = "startTime",required = false)@JsonFormat(pattern = "HH:mm") LocalTime startTime
            , @RequestParam(value = "endTime",required = false)@JsonFormat(pattern = "HH:mm") LocalTime endTime
            , @RequestParam(value = "company",required = false) String company
            , @RequestParam(value = "position",required = false) Position position) throws ResourceNotFoundException {
        List<InterviewerDto> interviewerDtoList = slotService.findInterviewers(industry,date,company,position,startTime,endTime);

    return new ResponseEntity<List<InterviewerDto>>(interviewerDtoList,HttpStatus.OK);
    }

}
