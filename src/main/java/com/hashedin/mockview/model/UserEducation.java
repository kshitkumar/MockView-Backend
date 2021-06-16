package com.hashedin.mockview.model;


import com.hashedin.mockview.dto.EducationCategory;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn
    private User user;

    private String degreeName;
    private String stream;
    private int marks;
    private int year;
    private String schoolName;
    private String country;

    @Enumerated(EnumType.STRING)
    private EducationCategory educationCategory;






}
