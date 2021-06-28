package com.hashedin.mockview.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hashedin.mockview.model.SlotStatus;
import lombok.*;

import java.sql.Date;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
    private int id;
    private LocalTime startTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalTime endTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date date;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SlotStatus slotStatus;
}
