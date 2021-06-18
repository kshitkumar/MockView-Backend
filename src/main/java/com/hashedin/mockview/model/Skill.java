package com.hashedin.mockview.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private Type type;

    private String name;
    @Enumerated(EnumType.STRING)
    private Proficiency proficiency;

    @ManyToOne
    @JoinColumn
    private User user;

}
