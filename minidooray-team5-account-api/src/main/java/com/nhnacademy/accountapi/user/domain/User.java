package com.nhnacademy.accountapi.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        status = UserStatus.ACTIVE;
    }

    public void leave() {
        this.status = UserStatus.TERMINATE;
    }
}
