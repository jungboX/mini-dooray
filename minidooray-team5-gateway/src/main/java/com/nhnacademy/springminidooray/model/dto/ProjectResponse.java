package com.nhnacademy.springminidooray.model.dto;

import com.nhnacademy.springminidooray.model.Status;

public record ProjectResponse(
        Long id,
        String title,
        Status status
) {}
