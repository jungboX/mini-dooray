package com.minidooray.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minidooray.task.controller.task.TagController;
import com.minidooray.task.dto.tag.TagCreateRequest;
import com.minidooray.task.dto.tag.TagResponse;
import com.minidooray.task.dto.tag.TagUpdateRequest;
import com.minidooray.task.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// TagController에 대한 MockMvc 테스트
@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // TagService를 Mocking합니다.
    @MockitoBean
    private TagService tagService;

    private final Long PROJECT_ID = 1L;
    private final Long TAG_ID = 20L;
    // Task ID는 readProjectId의 첫 번째 인수로 사용되지만, 실제로는 Project ID와 동일한 값을 전달받음
    private final Long DUMMY_TASK_ID = 10L;

    private final TagResponse mockResponse = new TagResponse(
            TAG_ID,
            "Feature",
            PROJECT_ID
    );

    @DisplayName("POST /api/projects/{projectId}/tags - 태그 생성 성공 (201 Created)")
    @Test
    void testCreateTag_success() throws Exception {
        // given
        TagCreateRequest request = new TagCreateRequest("Bugfix");

        // Service Mocking
        when(tagService.create(eq(PROJECT_ID), any(TagCreateRequest.class)))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(post("/api/projects/{projectId}/tags", PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // HTTP 201 확인
                .andExpect(jsonPath("$.content").value("Feature"));

        verify(tagService, times(1)).create(eq(PROJECT_ID), any(TagCreateRequest.class));
    }

    @DisplayName("GET /api/projects/{projectId}/tags/{tagId} - 태그 단일 조회 성공 (200 OK)")
    @Test
    void testGetTag_success() throws Exception {
        // Service Mocking (readProjectId의 첫 번째 인수는 Project ID가 아닌 Task ID로 되어 있음, 컨트롤러 매핑을 따름)
        // Note: TaskController의 URL 구조가 /tasks/{taskId}로, TagController의 URL 구조는 /tags/{tagId}로 분리되어 있어 projectId를 taskid로 전달하는 readProjectId 메서드의 이름이 조금 혼동될 수 있습니다.
        when(tagService.readProjectId(eq(PROJECT_ID), eq(TAG_ID))) // 컨트롤러에서 projectId를 첫 번째 인수로 넘겨줌
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/projects/{projectId}/tags/{tagId}", PROJECT_ID, TAG_ID))
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.id").value(TAG_ID))
                .andExpect(jsonPath("$.projectId").value(PROJECT_ID));

        verify(tagService, times(1)).readProjectId(eq(PROJECT_ID), eq(TAG_ID));
    }

    @DisplayName("PATCH /api/projects/{projectId}/tags/{tagId} - 태그 수정 성공 (200 OK)")
    @Test
    void testPatchTag_success() throws Exception {
        // given
        Long newProjectId = 2L;
        TagUpdateRequest request = new TagUpdateRequest("Refactored", newProjectId);
        TagResponse updatedResponse = new TagResponse(TAG_ID, "Refactored", newProjectId);

        // Service Mocking
        when(tagService.updateTag(eq(TAG_ID), any(TagUpdateRequest.class)))
                .thenReturn(updatedResponse);

        // when & then
        // PATCH는 URL에서 tagId만 PathVariable로 받음 (projectId는 URL에 있으나 메서드 인수에 사용되지 않음)
        mockMvc.perform(put("/api/projects/{projectId}/tags/{tagId}", PROJECT_ID, TAG_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.content").value("Refactored"))
                .andExpect(jsonPath("$.projectId").value(newProjectId));

        verify(tagService, times(1)).updateTag(eq(TAG_ID), any(TagUpdateRequest.class));
    }

    @DisplayName("DELETE /api/projects/{projectId}/tags/{tagId} - 태그 삭제 성공 (204 No Content)")
    @Test
    void testDeleteTag_success() throws Exception {
        // Service Mocking
        doNothing().when(tagService).deleteTag(eq(TAG_ID), eq(PROJECT_ID));

        // when & then
        mockMvc.perform(delete("/api/projects/{projectId}/tags/{tagId}", PROJECT_ID, TAG_ID))
                .andExpect(status().isNoContent()); // HTTP 204 확인

        verify(tagService, times(1)).deleteTag(eq(TAG_ID), eq(PROJECT_ID));
    }
}