package com.minidooray.task.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ProjectMembers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "Projects_id")
    private Project project;

    private String username;

    @Enumerated(EnumType.STRING)
    private ProjectMemberRole role;

    public ProjectMember(Project project, String username, ProjectMemberRole role) {
        this.project = project;
        this.username = username;
        this.role = role;
    }
}
