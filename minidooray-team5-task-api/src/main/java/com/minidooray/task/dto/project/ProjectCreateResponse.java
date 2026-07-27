package com.minidooray.task.dto.project;

import com.minidooray.task.model.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectCreateResponse {
    private Long id;
    private String title;
    private ProjectStatus status;
    private String message;
}
