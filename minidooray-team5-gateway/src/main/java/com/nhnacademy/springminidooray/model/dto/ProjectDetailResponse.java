package com.nhnacademy.springminidooray.model.dto;

import com.nhnacademy.springminidooray.model.Status;

import java.util.List;

public record ProjectDetailResponse(
    Long id,
    String title,
    Status status,
    List<String> members,
    List<TaskSummaryResponse> tasks,
    List<MilestoneResponse> milestones,
    List<TagResponse> tags
) {}
