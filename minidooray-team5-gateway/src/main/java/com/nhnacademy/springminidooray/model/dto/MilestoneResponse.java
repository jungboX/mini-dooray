package com.nhnacademy.springminidooray.model.dto;

import java.time.LocalDateTime;

public record MilestoneResponse(
    Long id,
    String content,
    LocalDateTime startedAt,
    LocalDateTime endedAt,
    long projectId
) {}