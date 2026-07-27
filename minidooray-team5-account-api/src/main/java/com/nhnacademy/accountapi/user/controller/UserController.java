package com.nhnacademy.accountapi.user.controller;

import com.nhnacademy.accountapi.user.dto.UserRegisterRequest;
import com.nhnacademy.accountapi.user.dto.UserResponse;
import com.nhnacademy.accountapi.user.dto.UserUpdateRequest;
import com.nhnacademy.accountapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUser() {
        List<UserResponse> response = userService.getAllUser();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRegisterRequest request) {
        UserResponse response = userService.register(request);

        URI locationUrl = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(request.username())
                .toUri();

        return ResponseEntity.created(locationUrl).body(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String username) {
        UserResponse response = userService.getUser(username);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String username, @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.update(username, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<UserResponse> leaveUser(@PathVariable String username) {
        userService.leave(username);

        return ResponseEntity.ok().build();
    }
}
