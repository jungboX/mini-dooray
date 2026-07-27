package com.minidooray.task.dto.task;

import java.util.List;

public record TaskUpdateRequest (
        String title,      // null 가능
        String content,    // null 가능
        Long milestoneId,  // null 가능하도록 래퍼 타입 사용
        // List는 불변 리스트를 사용하거나, 새 리스트로 대체해야 함
        List<Long> tagIds
) {}