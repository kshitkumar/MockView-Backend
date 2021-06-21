package com.hashedin.mockview.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @OneToOne
    @JoinColumn
    @JsonIgnore
    private User user;

    private String city;
    private String state;
    private String country;
    private String pinCode;



}
