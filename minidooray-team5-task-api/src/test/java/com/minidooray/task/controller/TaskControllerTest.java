package com.minidooray.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minidooray.task.controller.task.TaskController;
import com.minidooray.task.dto.milestone.MilestoneResponse;
import com.minidooray.task.dto.tag.TagResponse;
import com.minidooray.task.dto.task.TaskCreateRequest;
import com.minidooray.task.dto.task.TaskDetailResponse;
import com.minidooray.task.dto.task.TaskResponse;
import com.minidooray.task.dto.task.TaskUpdateRequest;
import com.minidooray.task.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Service 계층을 Mocking하여 Controller 테스트에 필요한 의존성을 주입
    @MockitoBean
    private TaskService taskService;

    private final Long PROJECT_ID = 1L;
    private final Long TASK_ID = 10L;

    @DisplayName("POST /api/projects/{projectId}/tasks - Task 생성 성공")
    @Test
    void testCreateTask_success() throws Exception {
        // given
        TaskCreateRequest request = new TaskCreateRequest("새 Task", "내용", "userA", 5L);
        TaskResponse mockResponse = new TaskResponse(
                "새 Task", "내용", "userA", 5L);

        // Service 계층 Mocking: createTask 호출 시 mockResponse 반환
        when(taskService.createTask(eq(PROJECT_ID), any(TaskCreateRequest.class)))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(post("/api/projects/{projectId}/tasks", PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // HTTP 201 Created 확인
                .andExpect(jsonPath("$.title").value("새 Task"));

        // Service 메서드가 올바른 인수로 호출되었는지 검증
        verify(taskService, times(1)).createTask(eq(PROJECT_ID), any(TaskCreateRequest.class));
    }

    @DisplayName("GET /api/projects/{projectId}/tasks/{taskId} - Task 상세 조회 성공")
    @Test
    void testGetTask_success() throws Exception {
        // given
        MilestoneResponse milestoneResponse = new MilestoneResponse(
                20L, "Milestone A", null, null, PROJECT_ID);
        TaskDetailResponse mockResponse = new TaskDetailResponse(
                TASK_ID, "상세 제목", "상세 내용", "userA", milestoneResponse,
                List.of(new TagResponse(1L, "Bug", PROJECT_ID)), Collections.emptyList());

        // Service 계층 Mocking: getTask 호출 시 mockResponse 반환
        when(taskService.getTask(eq(PROJECT_ID), eq(TASK_ID)))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/projects/{projectId}/tasks/{taskId}", PROJECT_ID, TASK_ID))
                .andExpect(status().isOk()) // HTTP 200 OK 확인
                .andExpect(jsonPath("$.id").value(TASK_ID))
                .andExpect(jsonPath("$.milestone.content").value("Milestone A"));

        verify(taskService, times(1)).getTask(eq(PROJECT_ID), eq(TASK_ID));
    }

    @DisplayName("PATCH /api/projects/{projectId}/tasks/{taskId} - Task 수정 성공")
    @Test
    void testPatchTask_success() throws Exception {
        // given
        TaskUpdateRequest request = new TaskUpdateRequest("수정된 제목", null, 6L, null);
        TaskResponse mockResponse = new TaskResponse("수정된 제목", "원래 내용", "userA", 6L);

        // Service 계층 Mocking
        when(taskService.updateTask(eq(TASK_ID), any(TaskUpdateRequest.class)))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(put("/api/projects/{projectId}/tasks/{taskId}", PROJECT_ID, TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // HTTP 200 OK 확인
                .andExpect(jsonPath("$.title").value("수정된 제목"));

        verify(taskService, times(1)).updateTask(eq(TASK_ID), any(TaskUpdateRequest.class));
    }

    @DisplayName("DELETE /api/projects/{projectId}/tasks/{taskId} - Task 삭제 성공")
    @Test
    void testDeleteTask_success() throws Exception {
        // Service 계층 Mocking: deleteTask 호출 시 아무것도 반환하지 않음
        doNothing().when(taskService).deleteTask(eq(PROJECT_ID), eq(TASK_ID));

        // when & then
        mockMvc.perform(delete("/api/projects/{projectId}/tasks/{taskId}", PROJECT_ID, TASK_ID))
                .andExpect(status().isNoContent()); // HTTP 204 No Content 확인

        verify(taskService, times(1)).deleteTask(eq(PROJECT_ID), eq(TASK_ID));
    }
}