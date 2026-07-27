package com.minidooray.task.dto.task;

import com.minidooray.task.dto.comment.CommentResponse;
import com.minidooray.task.dto.milestone.MilestoneResponse;
import com.minidooray.task.dto.tag.TagResponse;

import java.util.List;

public record TaskDetailResponse(
        long id,
        String title,
        String content,
        String username,
        MilestoneResponse milestone,
        List<TagResponse> tags, // 태그 목록
        List<CommentResponse> commentList // 댓글 목록
)
{ }
