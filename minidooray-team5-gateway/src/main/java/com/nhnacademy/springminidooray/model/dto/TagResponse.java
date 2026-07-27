package com.nhnacademy.springminidooray.model.dto;

public record TagResponse(
    long id,
    String content,
    long projectId
) {}
