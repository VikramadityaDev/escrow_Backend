package com.escrowpj.escrow.controller;

import com.escrowpj.escrow.dto.ApiResponse;
import com.escrowpj.escrow.dto.CreateProjectRequest;
import com.escrowpj.escrow.dto.ProjectResponse;
import com.escrowpj.escrow.entity.Project;
import com.escrowpj.escrow.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // 🔹 CLIENT → Create Project with milestones
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Project>> createProject(
            @RequestBody CreateProjectRequest request,
            Principal principal
    ) {

        Project savedProject =
                projectService.createProject(principal.getName(), request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, savedProject)
        );
    }

    // 🔹 CLIENT → View Own Projects
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Project>>> getMyProjects(
            Principal principal
    ) {

        List<Project> projects =
                projectService.getByClient(principal.getName());

        return ResponseEntity.ok(
                new ApiResponse<>(true, projects)
        );
    }

    // 🔹 FREELANCER → View Available Projects
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<Project>>> getAvailableProjects() {

        List<Project> projects =
                projectService.getAvailableProjects();

        return ResponseEntity.ok(
                new ApiResponse<>(true, projects)
        );
    }

    // 🔹 FREELANCER → View Assigned Projects
    @GetMapping("/assigned")
    public ResponseEntity<ApiResponse<?>> getAssignedProjects(Principal principal) {

        return ResponseEntity.ok(
                new ApiResponse<>(true,
                        projectService.getAssignedProjects(principal.getName()))
        );
    }
}
