package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.Slot;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SlotDto {
    private List<Slot> slotList;
    private Integer interviewCharges;
}
