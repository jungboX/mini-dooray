package com.minidooray.task.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectMemberRole {
    ADMIN("관리자"),
    MEMBER("멤버");

    private final String displayName;

    public String displayName(){
        return displayName;
    }
}
