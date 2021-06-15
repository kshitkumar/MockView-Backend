package com.hashedin.mockview.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hashedin.mockview.dto.Gender;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;

    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    private Gender gender;

    @Column(unique = true)
    private String emailId;

    @NotNull
    private String password;

    @Size(min = 10, max = 10)
    @Column(length = 10)
    private String phoneNumber;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private UserProfile userProfile;
}
