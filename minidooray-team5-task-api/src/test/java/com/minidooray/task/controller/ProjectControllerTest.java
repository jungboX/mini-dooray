package com.minidooray.task.controller;

import com.minidooray.task.controller.project.ProjectController;
import com.minidooray.task.dto.project.*;
import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectMember;
import com.minidooray.task.model.ProjectStatus;
import com.minidooray.task.service.MilestoneService;
import com.minidooray.task.service.ProjectService;
import com.minidooray.task.service.TagService;
import com.minidooray.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @InjectMocks
    private ProjectController controller;

    @Mock
    private ProjectService projectService;

    private Project project;

    @Mock
    private TaskService taskService;

    @Mock
    private MilestoneService milestoneService;

    @Mock
    private TagService tagService;

    @BeforeEach
    void setUp() {
        project = new Project("테스트 프로젝트", ProjectStatus.ACTIVE);
    }

    @DisplayName("내 프로젝트 목록 조회 - 성공")
    @Test
    void getMyProjects_success() {
        ProjectMember member = new ProjectMember(project, "adminUser", null);
        when(projectService.getMyProjects("adminUser"))
                .thenReturn(List.of(member));

        ResponseEntity<List<ProjectSummaryResponse>> response = controller.getMyProjects("adminUser");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().getFirst().getTitle()).isEqualTo("테스트 프로젝트");
    }

    @DisplayName("프로젝트 생성 - 성공")
    @Test
    void createProject_success() {
        when(projectService.createProject("adminUser", "새 프로젝트", ProjectStatus.ACTIVE))
                .thenReturn(project);

        ProjectCreateRequest request = new ProjectCreateRequest("새 프로젝트", ProjectStatus.ACTIVE, "adminUser");
        ResponseEntity<ProjectCreateResponse> response = controller.createProject(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @DisplayName("프로젝트 상세 조회 - 성공")
    @Test
    void getProjectDetail_success() {
        ProjectMember member = new ProjectMember(project, "adminUser", null);
        project.getProjectMembers().add(member);

        when(projectService.getProjectById(1L)).thenReturn(project);

        ResponseEntity<ProjectDetailResponse> response = controller.getProjectDetail(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("존재하지 않는 프로젝트 상세 조회 - 실패")
    @Test
    void getNotExistProjectDetail_fail() {
        when(projectService.getProjectById(999L)).thenThrow(new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

        assertThrows(IllegalArgumentException.class, () ->
                controller.getProjectDetail(999L)
        );

    }

    @DisplayName("프로젝트 상태 변경 - 성공")
    @Test
    void patchProjectStatus_success() {
        doNothing().when(projectService).changeStatus(1L, ProjectStatus.SLEEP);

        ProjectStatusChangeRequest request = new ProjectStatusChangeRequest(ProjectStatus.SLEEP);
        ResponseEntity<ProjectStatusChangeRequest> response =
                controller.patchProjectStatus(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
