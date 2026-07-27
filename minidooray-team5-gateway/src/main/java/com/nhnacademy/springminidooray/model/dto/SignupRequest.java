package com.nhnacademy.springminidooray.model.dto;

public record SignupRequest(
    String username,
    String password,
    String email
) {}
