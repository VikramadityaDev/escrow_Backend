package com.escrowpj.escrow.controller;

import com.escrowpj.escrow.dto.*;
import com.escrowpj.escrow.entity.FreelancerProfile;
import com.escrowpj.escrow.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // 🔹 Freelancer creates/updates profile
    @PostMapping
    public ResponseEntity<?> saveProfile(
            @RequestBody ProfileRequest request,
            Principal principal
    ){

        FreelancerProfile profile =
                profileService.saveProfile(principal.getName(), request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, profile)
        );
    }

    // 🔹 Client views freelancer profile
    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable Long userId){

        return ResponseEntity.ok(
                new ApiResponse<>(true,
                        profileService.getProfile(userId))
        );
    }
}