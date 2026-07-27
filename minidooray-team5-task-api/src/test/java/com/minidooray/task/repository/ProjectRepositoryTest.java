package com.minidooray.task.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectStatus;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    private Project savedProject;

    @BeforeEach
    void setUp(){
        Project project = new Project("테스트용", ProjectStatus.ACTIVE);
        savedProject = projectRepository.saveAndFlush(project);
    }

    @DisplayName("프로젝트 생성 후 ID로 조회시 동일 데이터 반환")
    @Test
    void projectSaveAndFindTest() {
        Optional<Project> foundProject = projectRepository.findById(savedProject.getId());

        assertThat(foundProject).isPresent();
        assertThat(foundProject.get().getTitle()).isEqualTo("테스트용");
        assertThat(foundProject.get().getStatus()).isEqualTo(ProjectStatus.ACTIVE);
    }
    @DisplayName("프로젝트 삭제시 ID로 조회 불가")
    @Test
    void projectDeleteTest() {
        projectRepository.delete(savedProject);

        assertThat(projectRepository.findById(savedProject.getId())).isEmpty();
    }

    @DisplayName("프로젝트 제목 및 상태 업데이트 성공")
    @Test
    void projectUpdateTest() {
        Project project = projectRepository.findById(savedProject.getId()).orElseThrow();

        project.setTitle("제목 수정 프로젝트");
        project.setStatus(ProjectStatus.TERMINATE);
        projectRepository.saveAndFlush(project);

        Project updated = projectRepository.findById(savedProject.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("제목 수정 프로젝트");
        assertThat(updated.getStatus()).isEqualTo(ProjectStatus.TERMINATE);
    }

    @DisplayName("존재하지 않는 프로젝트 ID 조회 시 empty")
    @Test
    void notExistFindById_empty() {
        Optional<Project> result = projectRepository.findById(9999L);

        assertThat(result).isEmpty();
    }

    @DisplayName("프로젝트 전체 조회 성공")
    @Test
    void findAllProjectsTest() {
        projectRepository.save(new Project("프로젝트1", ProjectStatus.ACTIVE));
        projectRepository.save(new Project("프로젝트2", ProjectStatus.SLEEP));
        projectRepository.save(new Project("프로젝트3", ProjectStatus.TERMINATE));

        List<Project> projects = projectRepository.findAll();

        assertThat(projects).hasSizeGreaterThanOrEqualTo(4); // setUp에 있는거 포함
        assertThat(projects)
                .extracting(Project::getTitle)
                .contains("테스트용", "프로젝트1", "프로젝트2", "프로젝트3");
    }
}
