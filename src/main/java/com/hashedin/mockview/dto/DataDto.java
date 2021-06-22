package com.hashedin.mockview.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataDto {
    private List<String> values;
}
