package com.nhnacademy.springminidooray.model.dto;

public record CommentCreateRequest(
    String username,
    String content
) {}
