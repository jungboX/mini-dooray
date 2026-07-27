package com.nhnacademy.accountapi.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.accountapi.user.dto.UserRegisterRequest;
import com.nhnacademy.accountapi.user.dto.UserResponse;
import com.nhnacademy.accountapi.user.dto.UserUpdateRequest;
import com.nhnacademy.accountapi.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("사용자 등록 - 성공")
    void register_Success() throws Exception {
        UserRegisterRequest registerRequest = new UserRegisterRequest("testUser", "1234", "testUser@example.com");
        UserResponse userResponse = new UserResponse("testUser", "testUser@example.com");

        when(userService.register(any(UserRegisterRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(userResponse.username()))
                .andExpect(jsonPath("$.email").value(userResponse.email())
        );

        mockMvc.perform(get("/api/users/testUser"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자 수정 - 성공")
    void update_Success() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest("1234", "testUpdate@example.com");
        UserResponse userResponse = new UserResponse("testUser", "testUpdate@example.com");

        when(userService.update(any(String.class), any(UserUpdateRequest.class))).thenReturn(userResponse);

        mockMvc.perform(put("/api/users/testUser")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userResponse.username()))
                .andExpect(jsonPath("$.email").value(userResponse.email())
        );
    }

    @Test
    @DisplayName("사용자 탈퇴 - 성공")
    void leave_Success() throws Exception {
        mockMvc.perform(delete("/api/users/testUser"))
                .andExpect(status().isOk());
    }
}