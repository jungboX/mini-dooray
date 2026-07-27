package com.minidooray.task.dto.project;

import com.minidooray.task.model.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreateRequest {
    private String title;
    private ProjectStatus status;
    private String username;
}
