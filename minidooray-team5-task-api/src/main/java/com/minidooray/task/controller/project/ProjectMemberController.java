package com.minidooray.task.controller.project;

import com.minidooray.task.dto.project.ProjectMemberAddRequest;
import com.minidooray.task.dto.project.ProjectMemberRemoveRequest;
import com.minidooray.task.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @PostMapping("/{projectId}/members")
    public ResponseEntity<String> addMember(@PathVariable Long projectId,
                                            @RequestBody ProjectMemberAddRequest request) {
        projectMemberService.addMember(
                projectId,
                request.getAdminUsername(),
                request.getNewMemberUsername()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body("멤버가 추가되었습니다.");
    }

    @DeleteMapping("/{projectId}/members/{username}")
    public ResponseEntity<String> removeMember(@PathVariable Long projectId,
                                               @PathVariable String username,
                                               @RequestBody ProjectMemberRemoveRequest request) {

        projectMemberService.removeMember(
                projectId,
                request.getAdminUsername(),
                username
        );
        return ResponseEntity.ok("멤버가 삭제되었습니다.");
    }
}