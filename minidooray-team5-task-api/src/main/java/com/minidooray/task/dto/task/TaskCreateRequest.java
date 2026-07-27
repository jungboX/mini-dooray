package com.minidooray.task.dto.task;


public record TaskCreateRequest(
        String title,
        String content,
        String username,
        Long milestoneId
//        Project project,
//        Milestone milestone
) {}