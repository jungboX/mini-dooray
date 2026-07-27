package com.nhnacademy.springminidooray.model;

public enum Status {
    ACTIVE("활성"),
    SLEEP("휴면"),
    TERMINATE("종료");

    private final String displayName;

    Status(String string) {
        this.displayName = string;
    }

    public String getDisplayName() {
        return displayName;
    }
}
