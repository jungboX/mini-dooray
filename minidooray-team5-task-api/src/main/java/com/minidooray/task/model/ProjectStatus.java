package com.minidooray.task.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectStatus {
    ACTIVE("활성"),
    SLEEP("휴면"),
    TERMINATE("종료");

    private final String displayName;

    public String displayName(){
        return displayName;
    }
}
