package com.minidooray.task.dto.milestone;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MilestoneCreateRequest {
    private String content;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private long projectId;
    private String username;

}