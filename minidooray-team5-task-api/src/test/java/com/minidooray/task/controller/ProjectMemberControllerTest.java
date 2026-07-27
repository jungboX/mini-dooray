package com.minidooray.task.controller;

import com.minidooray.task.controller.project.ProjectMemberController;
import com.minidooray.task.dto.project.ProjectMemberAddRequest;
import com.minidooray.task.dto.project.ProjectMemberRemoveRequest;
import com.minidooray.task.service.ProjectMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ProjectMemberControllerTest {

    @InjectMocks
    private ProjectMemberController controller;

    @Mock
    private ProjectMemberService projectMemberService;

    @DisplayName("관리자 - 멤버 추가 성공")
    @Test
    void addMemberAdmin_success() {
        ProjectMemberAddRequest request = new ProjectMemberAddRequest("adminUser", "memberUser");
        doNothing().when(projectMemberService)
                .addMember(1L, "adminUser", "memberUser");

        ResponseEntity<String> response = controller.addMember(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @DisplayName("비관리자 - 멤버 추가 실패")
    @Test
    void addMemberNotAdmin_fail() {
        ProjectMemberAddRequest request = new ProjectMemberAddRequest("memberUser", "memberUser2");
        doThrow(new SecurityException("관리자만 멤버를 추가할 수 있습니다."))
                .when(projectMemberService)
                .addMember(1L, "memberUser", "memberUser2");

        assertThrows(SecurityException.class, () ->
                controller.addMember(1L, request)
        );
    }

    @DisplayName("관리자 - 멤버 삭제 성공")
    @Test
    void removeMemberAdmin_success() {
        ProjectMemberRemoveRequest request = new ProjectMemberRemoveRequest("adminUser", "memberUser");

        doNothing().when(projectMemberService)
                .removeMember(1L, "adminUser", "memberUser");

        ResponseEntity<String> response = controller.removeMember(1L, "memberUser", request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("비관리자 - 멤버 삭제 실패")
    @Test
    void removeMemberNotAdmin_fail() {
        ProjectMemberRemoveRequest request = new ProjectMemberRemoveRequest("memberUser", "memberUser2");

        doThrow(new SecurityException("관리자만 멤버를 삭제할 수 있습니다."))
                .when(projectMemberService)
                .removeMember(1L, "memberUser", "memberUser");

        assertThrows(SecurityException.class, () ->
                controller.removeMember(1L, "memberUser", request)
        );
    }

    @DisplayName("존재하지 않는 프로젝트에서 멤버 추가 실패")
    @Test
    void addMemberNotExistProject_fail() {
        ProjectMemberAddRequest request = new ProjectMemberAddRequest("adminUser", "targetUser");

        doThrow(new IllegalArgumentException("존재하지 않는 프로젝트입니다."))
                .when(projectMemberService)
                .addMember(999L, "adminUser", "targetUser");

        assertThrows(IllegalArgumentException.class, () ->
                controller.addMember(999L, request)
        );
    }
}
