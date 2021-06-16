package com.hashedin.mockview.model;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class UserUniversityEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String degreeName;
    private String stream;
    private float cgpa;
    private int yearOfGraduation;
    private String universityName;
    private String country;

    @ManyToOne
    @JoinColumn
    private UserEducation userEducation;

}
