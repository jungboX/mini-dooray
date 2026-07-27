package com.nhnacademy.accountapi.user.service;

import com.nhnacademy.accountapi.user.domain.User;
import com.nhnacademy.accountapi.user.dto.*;
import com.nhnacademy.accountapi.user.exception.UnauthorizedException;
import com.nhnacademy.accountapi.user.exception.UserAlreadyExistsException;
import com.nhnacademy.accountapi.user.exception.UserNotFoundException;
import com.nhnacademy.accountapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.existsById(request.username())) {
            throw new UserAlreadyExistsException("이미 존재하는 사용자: " + request.username());
        }

        User user = request.toEntity(passwordEncoder);

        userRepository.save(user);

        return new UserResponse(user.getUsername(), user.getEmail());
    }

    @Override
    public UserResponse getUser(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자: " + username));;

        return new UserResponse(username, user.getEmail());
    }

    @Override
    public List<UserResponse> getAllUser() {

        return userRepository.findAllBy();
    }

    @Override
    public UserResponse update(String username, UserUpdateRequest request) {
        if (userRepository.existsById(username)) {
            throw new UserNotFoundException("존재하지 않는 사용자: " + username);
        }

        User user = request.toEntity(username, passwordEncoder);

        userRepository.save(user);

        return new UserResponse(user.getUsername(), user.getEmail());
    }

    @Transactional
    @Override
    public void leave(String username) {
        userRepository.leaveUser(username);
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User user = userRepository.findById(request.username()).orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자: " + request.username()));

        return new UserLoginResponse(user.getPassword());
    }
}
