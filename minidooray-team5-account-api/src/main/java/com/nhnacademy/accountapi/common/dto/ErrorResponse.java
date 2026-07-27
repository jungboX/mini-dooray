package com.nhnacademy.accountapi.common.dto;

public record ErrorResponse(
    String code,
    String message
) {}
