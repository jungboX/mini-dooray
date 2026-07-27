package com.nhnacademy.springminidooray.service;

import com.nhnacademy.springminidooray.model.UserLoginRequest;
import com.nhnacademy.springminidooray.model.dto.UserLoginResponse;
import com.nhnacademy.springminidooray.model.dto.SignupRequest;
import com.nhnacademy.springminidooray.model.dto.UserRequest;
import com.nhnacademy.springminidooray.model.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountApiService {
    @Value("${api.account.url}")
    private String ACCOUNT_API_URL;

    private final RestTemplate restTemplate;

    public UserLoginResponse login(UserLoginRequest request) {
        String requestUrl = ACCOUNT_API_URL + "/api/login";

        try {
            return restTemplate.postForObject(requestUrl, request, UserLoginResponse.class);
        } catch (HttpClientErrorException e) {
            log.debug("로그인 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public boolean exists(String username) {
        String requestUrl = ACCOUNT_API_URL + "/api/users/" + username;

        try {
            restTemplate.getForEntity(requestUrl, UserResponse.class);

            return true;
        } catch (HttpClientErrorException e) {
            log.debug("존재하지 않는 유저: {}", username, e);
            throw e;
        }
    }

    public void createUser(SignupRequest request) {
        String requestUrl = ACCOUNT_API_URL + "/api/users";

        try {
            restTemplate.postForEntity(requestUrl, request, UserResponse.class);

        } catch (HttpClientErrorException e) {
            log.error("회원가입 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }
}
