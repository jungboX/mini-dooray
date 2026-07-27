package com.minidooray.task.dto.task;


public record TaskResponse (
        String title,
        String content,
        String username,
        Long milestoneId

) {}
