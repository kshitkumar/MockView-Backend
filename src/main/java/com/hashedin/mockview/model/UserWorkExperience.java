package com.hashedin.mockview.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserWorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn
    private User user;
    private String companyName;
    private String industry;
    private String role;
    private String responsibility;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joiningDate;
    private Date endingDate;
    private String currentEmployment;

}
