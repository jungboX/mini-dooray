package com.nhnacademy.accountapi.user.controller;

import com.nhnacademy.accountapi.user.dto.UserLoginRequest;
import com.nhnacademy.accountapi.user.dto.UserLoginResponse;
import com.nhnacademy.accountapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final UserService userService;

    @PostMapping("/api/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        UserLoginResponse response = userService.login(request);

        return ResponseEntity.ok(response);
    }
}
