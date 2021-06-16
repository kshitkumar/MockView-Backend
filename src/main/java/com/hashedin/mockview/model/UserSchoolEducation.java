package com.hashedin.mockview.model;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class UserSchoolEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int marks;
    private int year;
    private String schoolName;
    private String country;
    private String className;

    @ManyToOne
    @JoinColumn
    private UserEducation userEducation;
}
