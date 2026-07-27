package com.nhnacademy.springminidooray.model.dto;

import java.time.LocalDate;

public record TaskDto(
    int id,
    String title,
    String content,
    LocalDate started_at,
    LocalDate ended_at
) {}
