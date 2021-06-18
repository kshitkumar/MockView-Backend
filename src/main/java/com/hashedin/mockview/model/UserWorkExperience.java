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
    private String position;
    private String responsibility;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joiningDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endingDate;
    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private Boolean currentEmployment;

}
