package com.nhnacademy.springminidooray.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberRequest {
    private String adminUsername;
    private String newMemberUsername;
}
