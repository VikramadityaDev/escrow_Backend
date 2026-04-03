package com.escrowpj.escrow.service;

import com.escrowpj.escrow.dto.ProfileRequest;
import com.escrowpj.escrow.entity.*;
import com.escrowpj.escrow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final FreelancerProfileRepository profileRepository;
    private final UserRepository userRepository;

    public FreelancerProfile saveProfile(String email, ProfileRequest request){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getRole().name().equals("FREELANCER")){
            throw new RuntimeException("Only freelancers can create profile");
        }

        FreelancerProfile profile = profileRepository
                .findByUser(user)
                .orElse(new FreelancerProfile());

        profile.setUser(user);
        profile.setBio(request.getBio());
        profile.setSkills(request.getSkills());
        profile.setEducation(request.getEducation());

        return profileRepository.save(profile);
    }

    public FreelancerProfile getProfile(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }
}