package com.hashedin.mockview.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String category;
    private String organisation;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receivingDate;

    @Enumerated(EnumType.STRING)
    private AwardCategory awardCategory;

    @ManyToOne
    @JoinColumn
    private User user;

}
