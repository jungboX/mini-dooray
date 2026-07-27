package com.minidooray.task.dto.taskTag;


public record TaskTagResponse(
        long id,
        long taskId,
        long tagId
) {
}
