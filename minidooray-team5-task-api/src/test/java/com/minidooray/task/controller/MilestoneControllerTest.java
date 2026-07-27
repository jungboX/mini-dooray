package com.minidooray.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minidooray.task.dto.milestone.MilestoneCreateRequest;
import com.minidooray.task.dto.milestone.MilestoneResponse;
import com.minidooray.task.dto.milestone.MilestoneUpdateRequest;
import com.minidooray.task.service.MilestoneService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// MilestoneController에 대한 MockMvc 테스트
@WebMvcTest(MilestoneController.class)
class MilestoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // MilestoneService를 Mocking합니다.
    @MockitoBean
    private MilestoneService milestoneService;

    private final Long PROJECT_ID = 1L;
    private final Long MILESTONE_ID = 10L;

    private final MilestoneResponse mockResponse = new MilestoneResponse(
            MILESTONE_ID,
            "초기 마일스톤",
            LocalDateTime.of(2025, 1, 1, 0, 0),
            LocalDateTime.of(2025, 1, 31, 23, 59),
            PROJECT_ID
    );


    @DisplayName("POST /api/projects/{projectId}/milestones - 마일스톤 생성 성공 (201 Created)")
    @Test
    void testCreateMilestone_success() throws Exception {
        // given
        MilestoneCreateRequest request = new MilestoneCreateRequest(
                "새 마일스톤", null, null, PROJECT_ID, "userA");

        // Service Mocking
        when(milestoneService.createMilestone(eq(PROJECT_ID), any(MilestoneCreateRequest.class)))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(post("/api/projects/{projectId}/milestones", PROJECT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // HTTP 201 확인
                .andExpect(jsonPath("$.content").value("초기 마일스톤"));

        verify(milestoneService, times(1)).createMilestone(eq(PROJECT_ID), any(MilestoneCreateRequest.class));
    }

    @DisplayName("GET /api/projects/{projectId}/milestones/{milestoneId} - 마일스톤 단일 조회 성공 (200 OK)")
    @Test
    void testGetMilestone_success() throws Exception {
        // Service Mocking
        when(milestoneService.getMilestone(eq(PROJECT_ID), eq(MILESTONE_ID)))
                .thenReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/projects/{projectId}/milestones/{milestoneId}", PROJECT_ID, MILESTONE_ID))
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.id").value(MILESTONE_ID))
                .andExpect(jsonPath("$.content").value("초기 마일스톤"));

        verify(milestoneService, times(1)).getMilestone(eq(PROJECT_ID), eq(MILESTONE_ID));
    }

    @DisplayName("GET /api/projects/{projectId}/milestones - 마일스톤 목록 조회 성공 (200 OK)")
    @Test
    void testGetMilestones_success() throws Exception {
        // given
        List<MilestoneResponse> mockList = List.of(mockResponse,
                new MilestoneResponse(11L, "M2", null, null, PROJECT_ID));

        // Service Mocking
        when(milestoneService.getMilestones(eq(PROJECT_ID))).thenReturn(mockList);

        // when & then
        mockMvc.perform(get("/api/projects/{projectId}/milestones", PROJECT_ID))
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("초기 마일스톤"));

        verify(milestoneService, times(1)).getMilestones(eq(PROJECT_ID));
    }

    @DisplayName("PATCH /api/projects/{projectId}/milestones/{milestoneId} - 마일스톤 수정 성공 (200 OK)")
    @Test
    void testUpdateMilestone_success() throws Exception {
        // given
        MilestoneUpdateRequest request = new MilestoneUpdateRequest("수정됨", null, null);
        MilestoneResponse updatedResponse = new MilestoneResponse(
                MILESTONE_ID, "수정됨", mockResponse.getStartedAt(), mockResponse.getEndedAt(), PROJECT_ID);

        // Service Mocking
        when(milestoneService.updateMilestone(eq(PROJECT_ID), eq(MILESTONE_ID), any(MilestoneUpdateRequest.class)))
                .thenReturn(updatedResponse);

        // when & then
        mockMvc.perform(put("/api/projects/{projectId}/milestones/{milestoneId}", PROJECT_ID, MILESTONE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // HTTP 200 확인
                .andExpect(jsonPath("$.content").value("수정됨"));

        verify(milestoneService, times(1)).updateMilestone(eq(PROJECT_ID), eq(MILESTONE_ID), any(MilestoneUpdateRequest.class));
    }

    @DisplayName("DELETE /api/projects/{projectId}/milestones/{milestoneId} - 마일스톤 삭제 성공 (204 No Content)")
    @Test
    void testDeleteMilestone_success() throws Exception {
        // Service Mocking
        doNothing().when(milestoneService).deleteMilestone(eq(PROJECT_ID), eq(MILESTONE_ID));

        // when & then
        mockMvc.perform(delete("/api/projects/{projectId}/milestones/{milestoneId}", PROJECT_ID, MILESTONE_ID))
                .andExpect(status().isNoContent()); // HTTP 204 확인

        verify(milestoneService, times(1)).deleteMilestone(eq(PROJECT_ID), eq(MILESTONE_ID));
    }
}