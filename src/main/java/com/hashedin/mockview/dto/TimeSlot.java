package com.hashedin.mockview.dto;

import lombok.*;

import java.time.LocalTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
    private int id;
    private LocalTime startTime;
    private LocalTime endTime;

}
