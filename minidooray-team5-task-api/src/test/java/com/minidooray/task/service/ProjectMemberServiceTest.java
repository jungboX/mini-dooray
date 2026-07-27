package com.minidooray.task.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectMember;
import com.minidooray.task.model.ProjectMemberRole;
import com.minidooray.task.model.ProjectStatus;
import com.minidooray.task.repository.ProjectMemberRepository;
import com.minidooray.task.repository.ProjectRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProjectMemberServiceTest {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    private Project savedProject;
    @Autowired
    private ProjectMemberService projectMemberService;

    @BeforeEach
    void setUp(){
        Project project = new Project("테스트용", ProjectStatus.ACTIVE);
        savedProject = projectRepository.save(project);

        ProjectMember admin = new ProjectMember(savedProject, "adminUser", ProjectMemberRole.ADMIN);
        projectMemberRepository.save(admin);
    }

    @DisplayName("관리자 - 새로운 멤버 추가 성공")
    @Test
    void addMemberAdminTest(){
        projectMemberService.addMember(savedProject.getId(), "adminUser", "newUser");

        Optional<ProjectMember>
                result = projectMemberRepository.findByProjectIdAndUsername(savedProject.getId(), "newUser");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("newUser");
        assertThat(result.get().getRole()).isEqualTo(ProjectMemberRole.MEMBER);
    }

    @DisplayName("비관리자 - 새로운 멤버 추가 실패")
    @Test
    void addMemberNotAdminTest() {
        ProjectMember memberUser = new ProjectMember(savedProject, "memberUser", ProjectMemberRole.MEMBER);
        projectMemberRepository.save(memberUser);

        assertThrows(SecurityException.class, () ->
                projectMemberService.addMember(savedProject.getId(), "memberUser", "newUser"));
    }

    @DisplayName("관리자 - 멤버 삭제 성공")
    @Test
    void removeMember_success() {
        projectMemberService.addMember(savedProject.getId(), "adminUser", "newUser");
        projectMemberService.removeMember(savedProject.getId(), "adminUser", "newUser");

        assertThat(projectMemberRepository.findByProjectIdAndUsername(savedProject.getId(), "newUser")).isEmpty();
    }

    @DisplayName("비관리자 - 멤버 삭제 실패")
    @Test
    void removeMember_fail() {
        ProjectMember memberUser = new ProjectMember(savedProject, "memberUser", ProjectMemberRole.MEMBER);
        projectMemberRepository.save(memberUser);

        projectMemberService.addMember(savedProject.getId(), "adminUser", "newUser");

        assertThrows(SecurityException.class, () ->
                projectMemberService.removeMember(savedProject.getId(), "memberUser", "newUser"));
    }

    @DisplayName("이미 존재하는 멤버 추가 시 예외 발생")
    @Test
    void addMember_duplicate_throwsException() {
        projectMemberService.addMember(savedProject.getId(), "adminUser", "member1");

        assertThrows(IllegalStateException.class, () ->
                projectMemberService.addMember(savedProject.getId(), "adminUser", "member1"));
    }
}
