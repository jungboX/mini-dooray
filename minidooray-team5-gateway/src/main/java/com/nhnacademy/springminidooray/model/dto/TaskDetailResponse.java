package com.nhnacademy.springminidooray.model.dto;

import com.nhnacademy.springminidooray.model.Status;

import java.util.List;

public record TaskDetailResponse(
        long id,
        String title,
        String content,
        String username,
        MilestoneResponse milestone,
        List<TagResponse> tags,
        List<CommentResponse> commentList
) {}
