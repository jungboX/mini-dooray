package com.nhnacademy.accountapi.user.service;

import com.nhnacademy.accountapi.user.dto.*;

import java.util.List;


public interface UserService {
    UserResponse register(UserRegisterRequest request);
    UserResponse getUser(String username);
    UserResponse update(String username, UserUpdateRequest request);
    List<UserResponse> getAllUser();
    void leave(String username);
    UserLoginResponse login(UserLoginRequest request);
}
