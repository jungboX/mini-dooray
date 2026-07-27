package com.minidooray.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "task_Tags")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskTag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private long id;

    @ManyToOne
    @JoinColumn(name = "Task_id")
    private Task task; // fk

    @ManyToOne
    @JoinColumn(name = "Tag_id")
    private Tag tag; // fk

    // Tag 업데이트를 위한 핵심 메서드 추가
    public void updateTag(Tag newTag) {
        // 기존 Tag와의 연결을 끊고 (선택적)
        // newTag로 교체합니다.
        this.tag = newTag;
    }
}
