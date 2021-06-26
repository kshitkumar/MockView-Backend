package com.hashedin.mockview.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hashedin.mockview.model.Position;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class InterviewerDto {
    private int id;
    private String interviewerName;
    private String company;
    private Position position;
    private Date joiningDate;
    private Date endingDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double experience;
    private List<TimeSlot> timeSlots;
}
