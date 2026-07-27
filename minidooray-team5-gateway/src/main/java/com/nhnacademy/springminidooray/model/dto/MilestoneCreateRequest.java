package com.nhnacademy.springminidooray.model.dto;

import java.time.LocalDateTime;

public record MilestoneCreateRequest(
    String content,
    LocalDateTime startedAt,
    LocalDateTime endedAt,
    int projectId,
    String username
) {}
