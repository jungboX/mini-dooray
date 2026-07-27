package com.minidooray.task.dto.project;

import com.minidooray.task.model.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectStatusChangeRequest {
    private ProjectStatus newStatus;
}

