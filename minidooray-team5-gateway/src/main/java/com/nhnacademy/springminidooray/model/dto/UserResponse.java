package com.nhnacademy.springminidooray.model.dto;

// Account API 서버 응답 DTO
public record UserResponse(
    String username,
    String email
) { }
