package com.minidooray.task.controller.project;

import com.minidooray.task.dto.milestone.MilestoneResponse;
import com.minidooray.task.dto.project.ProjectCreateRequest;
import com.minidooray.task.dto.project.ProjectCreateResponse;
import com.minidooray.task.dto.project.ProjectDetailResponse;
import com.minidooray.task.dto.project.ProjectStatusChangeRequest;
import com.minidooray.task.dto.project.ProjectSummaryResponse;
import com.minidooray.task.dto.tag.TagResponse;
import com.minidooray.task.dto.task.TaskSummaryResponse;
import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectMember;
import com.minidooray.task.service.MilestoneService;
import com.minidooray.task.service.ProjectService;
import java.util.ArrayList;
import java.util.List;

import com.minidooray.task.service.TagService;
import com.minidooray.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final MilestoneService milestoneService;
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<ProjectSummaryResponse>> getMyProjects(@RequestParam String username) {
        List<ProjectMember> projectMembers = projectService.getMyProjects(username);

        List<ProjectSummaryResponse> response = new ArrayList<>();
        for (ProjectMember pm : projectMembers) {
            ProjectSummaryResponse summary = new ProjectSummaryResponse(
                    pm.getProject().getId(),
                    pm.getProject().getTitle(),
                    pm.getProject().getStatus()
            );
            response.add(summary);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProjectCreateResponse> createProject(@RequestBody ProjectCreateRequest request){
        Project project = projectService.createProject(request.getUsername(), request.getTitle(), request.getStatus());
        ProjectCreateResponse response = new ProjectCreateResponse(
                project.getId(),
                project.getTitle(),
                project.getStatus(),
                "프로젝트가 생성되었습니다."
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> getProjectDetail(@PathVariable Long projectId){
        Project project = projectService.getProjectById(projectId);

        List<String> members = new ArrayList<>();
        List<TaskSummaryResponse> tasks = taskService.getTaskList(projectId);


        for (ProjectMember pm : project.getProjectMembers()) {
            members.add(pm.getUsername());
        }

        List<MilestoneResponse> milestones = milestoneService.getMilestones(projectId);

        List<TagResponse> tags = tagService.readTagList(projectId);

        ProjectDetailResponse response = new ProjectDetailResponse(
                project.getId(),
                project.getTitle(),
                project.getStatus(),
                members,
                tasks,
                milestones,
                tags
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}/status")
    public ResponseEntity<ProjectStatusChangeRequest> patchProjectStatus(@PathVariable Long projectId,
                                                                         @RequestBody ProjectStatusChangeRequest request){
        projectService.changeStatus(projectId, request.getNewStatus());
        return ResponseEntity.noContent().build();
    }
}
