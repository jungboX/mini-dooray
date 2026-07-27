package com.minidooray.task.service;

import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectMember;
import com.minidooray.task.model.ProjectMemberRole;
import com.minidooray.task.repository.ProjectMemberRepository;
import com.minidooray.task.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    // 프로젝트 관리자는 멤버를 등록할 수 있다
    public void addMember(Long projectId, String adminUsername, String newMemberUsername) {

        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
        }

        ProjectMember adminMember = projectMemberRepository.findByProjectIdAndUsername(projectId, adminUsername)
                .orElseThrow(() -> new SecurityException("해당 프로젝트에 관리자가 존재하지 않습니다."));

        if (adminMember.getRole() != ProjectMemberRole.ADMIN) {
            throw new SecurityException("관리자만 멤버를 추가할 수 있습니다.");
        }

        ProjectMember existingMember = projectMemberRepository.findByProjectIdAndUsername(projectId, newMemberUsername).orElse(null);
        if (existingMember != null) {
            throw new IllegalStateException("이미 등록된 멤버입니다.");
        }

        ProjectMember newMember = new ProjectMember();
        newMember.setProject(project);
        newMember.setUsername(newMemberUsername);
        newMember.setRole(ProjectMemberRole.MEMBER);

        projectMemberRepository.save(newMember);
        project.addMember(newMember);
    }

    @Transactional
    public void removeMember(Long projectId, String adminUsername, String targetUsername) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new IllegalArgumentException("존재하지 않는 프로젝트입니다.");
        }

        ProjectMember admin = projectMemberRepository.findByProjectIdAndUsername(projectId, adminUsername).orElse(null);
        if (admin == null || admin.getRole() != ProjectMemberRole.ADMIN) {
            throw new SecurityException("관리자만 멤버를 삭제할 수 있습니다.");
        }

        ProjectMember target = projectMemberRepository.findByProjectIdAndUsername(projectId, targetUsername).orElse(null);
        if (target == null) {
            throw new IllegalArgumentException("삭제할 멤버가 존재하지 않습니다.");
        }

        projectMemberRepository.delete(target);
    }

}
