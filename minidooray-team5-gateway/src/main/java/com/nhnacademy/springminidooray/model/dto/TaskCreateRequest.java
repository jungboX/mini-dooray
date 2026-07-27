package com.nhnacademy.springminidooray.model.dto;

public record TaskCreateRequest(
        String title,
        String content,
        String username,
        int projectId,
        Integer milestoneId
) {}
