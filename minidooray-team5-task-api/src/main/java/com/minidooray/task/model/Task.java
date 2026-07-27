package com.minidooray.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Entity
@Table(name = "Tasks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", length = 45, nullable = false)
    private String title;

    @Column(name = "content", length = 200)
    private String content;

    @Column(name = "username", length = 45, nullable = false)
    private String username;

    // fk
    @ManyToOne
    @JoinColumn(name = "Projects_id", nullable = false)
    private Project project;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Milestones_id")
    private Milestone milestone;

    // 다대다를 위한 관계, task 하나가 여러 taskTag를 가질 수 있음.
    //  task를 참조하는 모든 자식 레코드(댓글, TaskTag 등)를 먼저 삭제
    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TaskTag> taskTags = new ArrayList<>();

    public Task(long id, String title, String content, String username, Project project, Milestone milestone) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.project = project;
        this.milestone = milestone;
        this.taskTags = new ArrayList<>();
    }
}
