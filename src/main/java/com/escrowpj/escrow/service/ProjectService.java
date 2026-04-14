package com.escrowpj.escrow.service;

import com.escrowpj.escrow.dto.CreateProjectRequest;
import com.escrowpj.escrow.dto.MilestoneResponse;
import com.escrowpj.escrow.dto.ProjectResponse;
import com.escrowpj.escrow.entity.*;
import com.escrowpj.escrow.exception.ProjectCreationException;
import com.escrowpj.escrow.exception.UserNotFoundException;
import com.escrowpj.escrow.repository.ProjectRepository;
import com.escrowpj.escrow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // CLIENT creates project with milestones
    public Project createProject(String email, CreateProjectRequest request) {

        User client = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!client.getRole().name().equals("CLIENT")) {
            throw new ProjectCreationException("Only CLIENT can create projects");
        }

        if (request.getDeadline() == null) {
            throw new ProjectCreationException("Project deadline is required");
        }

        LocalDate deadline = LocalDate.parse(request.getDeadline());

        if (deadline.isBefore(LocalDate.now())) {
            throw new ProjectCreationException("Deadline cannot be in the past");
        }

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setCategory(request.getCategory());
        project.setBudget(request.getBudget());
        project.setDeadline(deadline);
        project.setClient(client);
        project.setFreelancer(null);
        project.setStatus(ProjectStatus.CREATED);

        List<ProjectPhase> phases = request.getMilestones().stream().map(m -> {

            LocalDate milestoneDate = LocalDate.parse(m.getDueDate());

            if (milestoneDate.isAfter(deadline)) {
                throw new ProjectCreationException(
                        "Milestone due date cannot exceed project deadline"
                );
            }

            ProjectPhase phase = new ProjectPhase();
            phase.setName(m.getName());
            phase.setAmount(m.getAmount());
            phase.setDueDate(milestoneDate);
            phase.setStatus(PhaseStatus.PENDING);
            phase.setProject(project);

            return phase;

        }).collect(Collectors.toList());

        project.setMilestones(phases);

        return projectRepository.save(project);
    }

    // CLIENT → view own projects
    public List<Project> getByClient(String email) {

        User client = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return projectRepository.findByClient(client);
    }

    // FREELANCER → view available projects
    public List<Project> getAvailableProjects() {
        return projectRepository.findByFreelancerIsNull();
    }

    // FREELANCER → view assigned projects
    public List<ProjectResponse> getAssignedProjects(String email) {

        User freelancer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return projectRepository.findByFreelancer(freelancer)
                .stream()
                .map(project -> {

                    // 🔥 convert milestones safely
                    List<MilestoneResponse> milestoneList =
                            project.getMilestones().stream()
                                    .map(m -> new MilestoneResponse(
                                            m.getId(),
                                            m.getName(),
                                            m.getAmount(),
                                            m.getStatus().name(),
                                            m.getUpdateMessage()
                                    ))
                                    .collect(Collectors.toList());

                    return new ProjectResponse(
                            project.getId(),
                            project.getTitle(),
                            project.getDescription(),
                            project.getCategory(),
                            project.getBudget(),
                            project.getDeadline(),
                            project.getStatus().name(),
                            project.getClient().getName(),
                            project.getFreelancer().getName(),
                            milestoneList // 🔥 attach milestones
                    );

                })
                .collect(Collectors.toList());
    }
}
