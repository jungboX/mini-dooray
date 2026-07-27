package com.minidooray.task.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectMember;
import com.minidooray.task.model.ProjectMemberRole;
import com.minidooray.task.model.ProjectStatus;
import com.minidooray.task.repository.ProjectMemberRepository;
import com.minidooray.task.repository.ProjectRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    private Project savedProject;

    @BeforeEach
    void setUp(){
        Project project = new Project("테스트용", ProjectStatus.ACTIVE);
        savedProject = projectRepository.save(project);

        ProjectMember admin = new ProjectMember(savedProject, "adminUser", ProjectMemberRole.ADMIN);
        projectMemberRepository.save(admin);

    }

    @DisplayName("프로젝트 생성 성공")
    @Test
    void createProjectTest(){
        Project newProject = projectService.createProject("username", "새 프로젝트", ProjectStatus.SLEEP);
        assertThat(newProject.getTitle()).isEqualTo("새 프로젝트");
        assertThat(newProject.getStatus()).isEqualTo(ProjectStatus.SLEEP);
    }

    @DisplayName("프로젝트 상태 변경 성공")
    @Test
    void changeStatusTest(){
        projectService.changeStatus(savedProject.getId(), ProjectStatus.TERMINATE);
        Project updateStatusProject = projectRepository.findById(savedProject.getId()).orElseThrow();

        assertThat(updateStatusProject.getStatus()).isEqualTo(ProjectStatus.TERMINATE);
    }

    @DisplayName("존재하지 않는 프로젝트 상태 변경 시 실패")
    @Test
    void changeStatusTest_fail() {
        assertThrows(IllegalArgumentException.class, () ->
                projectService.changeStatus(9999L, ProjectStatus.SLEEP)
        );
    }

    @DisplayName("사용자 프로젝트 목록 조회 성공")
    @Test
    void getMyProjectTest(){
        List<ProjectMember> projects = projectService.getMyProjects("adminUser");

        assertThat(projects)
                .extracting(pm -> pm.getProject().getTitle())
                .contains("테스트용");
    }

    @DisplayName("존재하지 않는 프로젝트 ID 조회 시 예외 발생")
    @Test
    void getNotExistProjectById_fail() {
        assertThrows(IllegalArgumentException.class, () ->
                projectService.getProjectById(9999L)
        );
    }
}