package com.escrowpj.escrow.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private Double budget;
    private LocalDate deadline;
    private String status;

    private String clientName;
    private String freelancerName;

    private List<MilestoneResponse> milestones; // 🔥 important
}