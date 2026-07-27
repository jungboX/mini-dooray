package com.minidooray.task.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberRemoveRequest {
    private String adminUsername;
    private String newMemberUsername;
}

