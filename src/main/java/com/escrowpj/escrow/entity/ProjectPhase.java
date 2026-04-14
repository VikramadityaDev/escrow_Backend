package com.escrowpj.escrow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double amount;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private PhaseStatus status = PhaseStatus.PENDING;

    private String updateMessage;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}