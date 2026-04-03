package com.escrowpj.escrow.repository;

import com.escrowpj.escrow.entity.FreelancerProfile;
import com.escrowpj.escrow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, Long> {

    Optional<FreelancerProfile> findByUser(User user);
}