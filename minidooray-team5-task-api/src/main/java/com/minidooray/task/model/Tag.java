package com.minidooray.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Tags")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "content", length = 20)
    private String content;

    @ManyToOne
    @JoinColumn(name = "Projects_id")
    private Project project; // fk

    // 다대다를 위한 관계
    @OneToMany(mappedBy = "tag")
    private List<TaskTag> taskTags = new ArrayList<>();

    public Tag(long id, String content, Project project) {
        this.id = id;
        this.content = content;
        this.project = project;
        this.taskTags  = new ArrayList<>();
    }
}
