package com.minidooray.task.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskSummaryResponse {
    private long id;
    private String title;
}