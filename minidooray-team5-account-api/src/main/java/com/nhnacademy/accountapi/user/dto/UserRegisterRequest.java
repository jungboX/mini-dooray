package com.nhnacademy.accountapi.user.dto;

import com.nhnacademy.accountapi.user.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public record UserRegisterRequest(
    String username,
    String password,
    String email
) {
    public User toEntity(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password), email);
    }
}
