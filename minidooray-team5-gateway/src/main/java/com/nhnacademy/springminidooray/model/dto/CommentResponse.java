package com.nhnacademy.springminidooray.model.dto;

public record CommentResponse(
    long id,
    long taskId,
    String username,
    String content
) {}
