package com.nhnacademy.springminidooray.model.dto;

public record TaskResponse(
    String title,
    String content,
    String username
) {}
