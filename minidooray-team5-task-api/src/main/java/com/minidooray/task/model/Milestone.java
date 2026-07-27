package com.minidooray.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "Milestones")
@NoArgsConstructor
@AllArgsConstructor
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "content", length = 200)
    private String content;

    // started_at DATETIME
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    // ended_at DATETIME
    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Projects_id")
    private Project project;
    // 마일스톤 내용 update
    public void updateDetails(String content, LocalDateTime startedAt, LocalDateTime endedAt) {
        if (content != null) {
            this.content = content;
        }
        if (startedAt != null) {
            this.startedAt = startedAt;
        }
        if (endedAt != null) {
            this.endedAt = endedAt;
        }
    }

}
