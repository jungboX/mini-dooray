package com.minidooray.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Comments")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne // fk
    @JoinColumn(name = "Tasks_id")
    private Task task;

    @Column(name = "username", length = 45)
    private String username;

    @Column(name = "content", length = 200)
    private String content;


}
