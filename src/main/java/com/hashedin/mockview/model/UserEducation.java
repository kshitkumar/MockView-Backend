package com.hashedin.mockview.model;


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

    @OneToOne
    @JoinColumn
    private User user;

    @OneToMany(mappedBy = "userEducation",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<UserSchoolEducation> userSchoolEducation;

    @OneToMany(mappedBy = "userEducation",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<UserUniversityEducation> userUniversityEducations;




}
