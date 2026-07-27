package com.nhnacademy.springminidooray.model;

public record ProjectCreateRequest(
    String username,
    String title,
    Status status
) {}
