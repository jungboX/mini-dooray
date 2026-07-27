package com.minidooray.task.dto.milestone;

import java.time.LocalDateTime;

public record MilestoneUpdateRequest(
        String content,
        LocalDateTime startedAt,
        LocalDateTime endedAt
) {
}
