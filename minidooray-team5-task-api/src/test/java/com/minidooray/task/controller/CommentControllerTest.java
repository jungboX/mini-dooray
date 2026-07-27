package com.minidooray.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minidooray.task.dto.comment.CommentCreateRequest;
import com.minidooray.task.dto.comment.CommentResponse;
import com.minidooray.task.dto.comment.CommentUpdateRequest;
import com.minidooray.task.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// CommentController에 대한 MockMvc 테스트
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // CommentService를 Mocking합니다.
    @MockitoBean
    private CommentService commentService;

    private final Long PROJECT_ID = 1L;
    private final Long TASK_ID = 10L;
    private final Long COMMENT_ID = 100L;

    private final CommentResponse mockResponse = new CommentResponse(
            COMMENT_ID,
            TASK_ID,
            "commenterA",
            "초기 댓글 내용"
    );

    @DisplayName("POST /{taskId}/comments - 댓글 생성 성공 (201 Created)")
    @Test
    void testCreateComment_success() throws Exception {
        // given
        CommentCreateRequest request = new CommentCreateRequest("userB", "새로운 댓글입니다.");

        // Service Mocking
        when(commentService.createComment(eq(TASK_ID), any(CommentCreateRequest.class)))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(post("/api/projects/{projectId}/tasks/{taskId}/comments", PROJECT_ID, TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // HTTP 201 확인
                .andExpect(jsonPath("$.username").value("commenterA"))
                .andExpect(jsonPath("$.taskId").value(TASK_ID));

        verify(commentService, times(1)).createComment(eq(TASK_ID), any(CommentCreateRequest.class));
    }

    @DisplayName("GET /{taskId}/comments/{commentId} - 댓글 단일 조회 성공 (200 OK)")
    @Test
    void testGetComment_success() throws Exception {
        // Service Mocking
        when(commentService.getComment(eq(TASK_ID), eq(COMMENT_ID)))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/projects/{projectId}/tasks/{taskId}/comments/{commentId}", PROJECT_ID, TASK_ID, COMMENT_ID))
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.id").value(COMMENT_ID))
                .andExpect(jsonPath("$.content").value("초기 댓글 내용"));

        verify(commentService, times(1)).getComment(eq(TASK_ID), eq(COMMENT_ID));
    }

    @DisplayName("GET /{taskId}/comments - 댓글 목록 조회 성공 (200 OK)")
    @Test
    void testGetAllComments_success() throws Exception {
        // given
        CommentResponse mockResponse2 = new CommentResponse(101L, TASK_ID, "userC", "댓글 2");
        List<CommentResponse> mockList = List.of(mockResponse, mockResponse2);

        // Service Mocking
        when(commentService.getAllCommentsByTaskId(eq(TASK_ID))).thenReturn(mockList);

        // when & then
        mockMvc.perform(get("/api/projects/{projectId}/tasks/{taskId}/comments", PROJECT_ID, TASK_ID))
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].content").value("댓글 2"));

        verify(commentService, times(1)).getAllCommentsByTaskId(eq(TASK_ID));
    }

    @DisplayName("PATCH /{taskId}/comments/{commentId} - 댓글 수정 성공 (200 OK)")
    @Test
    void testUpdateComment_success() throws Exception {
        // given
        String newContent = "수정된 최종 내용입니다.";
        CommentUpdateRequest request = new CommentUpdateRequest(newContent);
        CommentResponse updatedResponse = new CommentResponse(
                COMMENT_ID, TASK_ID, "commenterA", newContent);

        // Service Mocking
        when(commentService.updateComment(eq(TASK_ID), eq(COMMENT_ID), eq(newContent)))
                .thenReturn(updatedResponse);

        // when & then
        mockMvc.perform(put("/api/projects/{projectId}/tasks/{taskId}/comments/{commentId}", PROJECT_ID, TASK_ID, COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.content").value(newContent));

        verify(commentService, times(1)).updateComment(eq(TASK_ID), eq(COMMENT_ID), eq(newContent));
    }

    @DisplayName("DELETE /{taskId}/comments/{commentId} - 댓글 삭제 성공 (204 No Content)")
    @Test
    void testDeleteComment_success() throws Exception {
        // Service Mocking
        doNothing().when(commentService).deleteComment(eq(TASK_ID), eq(COMMENT_ID));

        // when & then
        mockMvc.perform(delete("/api/projects/{projectId}/tasks/{taskId}/comments/{commentId}", PROJECT_ID, TASK_ID, COMMENT_ID))
                .andExpect(status().isNoContent()); // HTTP 204 확인

        verify(commentService, times(1)).deleteComment(eq(TASK_ID), eq(COMMENT_ID));
    }
}