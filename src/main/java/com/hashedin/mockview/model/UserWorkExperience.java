package com.hashedin.mockview.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private int id;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private User user;

    private String companyName;

    private Industry industry;
    private String role;

    private Position position;

    private String responsibility;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joiningDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endingDate;
    @Column(nullable = false, columnDefinition = "TINYINT", length = 1)
    private Boolean currentEmployment;

}
