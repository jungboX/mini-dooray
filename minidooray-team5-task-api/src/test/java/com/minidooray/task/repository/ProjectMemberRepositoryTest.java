package com.minidooray.task.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectMember;
import com.minidooray.task.model.ProjectMemberRole;
import com.minidooray.task.model.ProjectStatus;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProjectMemberRepositoryTest {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;
    @Autowired
    private ProjectRepository projectRepository;

    private Project savedProject;

    @BeforeEach
    void setUp(){
        Project project = new Project("테스트용", ProjectStatus.ACTIVE);
        savedProject = projectRepository.save(project);

        ProjectMember member1 = new ProjectMember(savedProject, "testUser1", ProjectMemberRole.ADMIN);
        ProjectMember member2 = new ProjectMember(savedProject, "testUser2", ProjectMemberRole.MEMBER);
        projectMemberRepository.save(member1);
        projectMemberRepository.save(member2);

    }

    @DisplayName("유저 이름으로 올바른 프로젝트 목록 반환")
    @Test
    void findByUsernameTest(){
        List <ProjectMember> result = projectMemberRepository.findByUsername("testUser1");

        assertThat(result.getFirst().getUsername()).isEqualTo("testUser1");
        assertThat(result.getFirst().getProject().getTitle()).isEqualTo("테스트용");
    }

    @DisplayName("프로젝트 ID로 그 프로젝트의 멤버 반환")
    @Test
    void findByProjectIdTest(){
        List<ProjectMember> result = projectMemberRepository.findByProjectId(savedProject.getId());

        assertThat(result.getFirst().getProject().getId()).isEqualTo(savedProject.getId());
    }

   @DisplayName("프로젝트 ID와 유저 이름으로 해당 프로젝트의 특정 멤버 반환")
    @Test
    void findByProjectIdAndUsernameTest(){
       Optional<ProjectMember> result = projectMemberRepository.findByProjectIdAndUsername(savedProject.getId(), "testUser2");

       assertThat(result).isPresent();
       assertThat(result.get().getUsername()).isEqualTo("testUser2");
       assertThat(result.get().getRole()).isEqualTo(ProjectMemberRole.MEMBER);
    }

    @DisplayName("존재하지 않는 유저 이름으로 검색 시 empty")
    @Test
    void findByUsernameNotExistTest() {
        List<ProjectMember> result = projectMemberRepository.findByUsername("iAmNotUser");
        assertThat(result).isEmpty();
    }

    @DisplayName("멤버 삭제 시 실제로 DB에서 제거")
    @Test
    void deleteMemberTest() {
        ProjectMember targetMember = projectMemberRepository.findByProjectIdAndUsername(savedProject.getId(), "testUser2").orElseThrow();

        projectMemberRepository.delete(targetMember);

        Optional<ProjectMember> deleted = projectMemberRepository.findByProjectIdAndUsername(savedProject.getId(), "testUser2");
        assertThat(deleted).isEmpty();
    }

}