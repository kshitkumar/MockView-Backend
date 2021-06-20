package com.hashedin.mockview.dto;

import com.hashedin.mockview.model.Award;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Builder
@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardRequest {
    List<Award> userAwardList;
}
