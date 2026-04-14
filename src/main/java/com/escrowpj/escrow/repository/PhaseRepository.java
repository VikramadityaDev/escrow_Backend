package com.escrowpj.escrow.repository;

import com.escrowpj.escrow.entity.ProjectPhase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhaseRepository extends JpaRepository<ProjectPhase, Long> {

    List<ProjectPhase> findByProjectId(Long projectId);
}