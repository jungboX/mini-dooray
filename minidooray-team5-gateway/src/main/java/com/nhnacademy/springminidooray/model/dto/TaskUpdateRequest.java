package com.nhnacademy.springminidooray.model.dto;

import java.util.List;

public record TaskUpdateRequest(
    String title,
    String content,
    Long milestoneId,
    List<Long> tagIds
) {}
