package com.escrowpj.escrow.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One user = one profile
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String bio;

    private String skills; // comma separated (later can be List)

    private String education;
}