package com.hashedin.mockview.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn
    private User interviewer;

    @ManyToOne
    @JoinColumn
    private User interviewee;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interviewDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime interviewStartTime;

    @Enumerated(EnumType.STRING)
    private SlotStatus slotStatus;

    private Integer interviewCharges;



}
