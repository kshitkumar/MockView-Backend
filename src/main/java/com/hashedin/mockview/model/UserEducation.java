package com.hashedin.mockview.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

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
    @JsonIgnore
    private int id;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
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
