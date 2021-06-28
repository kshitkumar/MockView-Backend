package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.Position;
import lombok.*;

import java.sql.Date;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyInterviewDto {
    private int id;
    private String name;
    private String company;
    private Position position;
    private Date startDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer amount;
}
