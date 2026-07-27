package com.minidooray.task.service;

import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectMember;
import com.minidooray.task.model.ProjectMemberRole;
import com.minidooray.task.model.ProjectStatus;
import com.minidooray.task.repository.ProjectMemberRepository;
import com.minidooray.task.repository.ProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;


    public Project createProject(String username, String title, ProjectStatus status) {
        Project project = new Project();
        project.setTitle(title);
        project.setStatus(status != null ? status : ProjectStatus.ACTIVE);
        Project saved = projectRepository.save(project);

        ProjectMember admin = new ProjectMember(saved, username, ProjectMemberRole.ADMIN);
        projectMemberRepository.save(admin);

        return saved;
    } // 이름 안넘기게 수정

    // 프로젝트 멤버는 프로젝트의 상태를 변경할 수 있다
    public void changeStatus(Long projectId, ProjectStatus newStatus){
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
        }

        List<ProjectMember> members = projectMemberRepository.findByProjectId(projectId);
        if (members.isEmpty()) {
            throw new IllegalStateException("프로젝트에 등록된 멤버가 없습니다.");
        }

        project.changeStatus(newStatus);
    }

    @Transactional(readOnly = true)
    public Project getProjectById(Long projectId){
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
        }
        return project;
    }

    // 멤버는 자신이 속한 프로젝트 목록만 확인 가능
    @Transactional(readOnly = true)
    public List<ProjectMember> getMyProjects(String username) {
        return projectMemberRepository.findByUsername(username);
    }
}
