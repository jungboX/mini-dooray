package com.minidooray.task.dto.comment;

public record CommentResponse (
        long id,
        long taskId,
        String username,
        String content

){
}
