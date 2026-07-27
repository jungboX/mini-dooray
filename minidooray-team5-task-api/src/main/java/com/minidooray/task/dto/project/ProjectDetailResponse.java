package com.minidooray.task.dto.project;

import com.minidooray.task.dto.milestone.MilestoneResponse;
import com.minidooray.task.dto.tag.TagResponse;
import com.minidooray.task.dto.task.TaskSummaryResponse;
import com.minidooray.task.model.ProjectStatus;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectDetailResponse {
    private Long id;
    private String title;
    private ProjectStatus status;
    private List<String> members;
    private List<TaskSummaryResponse> tasks;
    private List<MilestoneResponse> milestones;
    private List<TagResponse> tags;
}