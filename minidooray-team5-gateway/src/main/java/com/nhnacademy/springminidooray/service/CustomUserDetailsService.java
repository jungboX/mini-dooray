package com.nhnacademy.springminidooray.service;

import com.nhnacademy.springminidooray.exception.ResourceNotFoundException;
import com.nhnacademy.springminidooray.model.UserLoginRequest;
import com.nhnacademy.springminidooray.model.dto.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;

//로그인 처리할 때 사용자를 어디서 찾아야 하는지 알려줌
//검사나 성공/실패 처리, 정보관리는 하지 않음. -> 그래야 MySQL처럼 찾는 곳 바껴도 UserDetailsService만 바꾸면 되기 때문
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate;

    @Value("${api.account.url}")
    private String ACCOUNT_API_URL;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String url = ACCOUNT_API_URL + "/api/login";

        UserLoginRequest loginRequest = new UserLoginRequest(username);

        try {
            UserLoginResponse userDto = restTemplate.postForObject(url, loginRequest, UserLoginResponse.class);

            if (Objects.isNull(userDto)) {
                throw new ResourceNotFoundException("User Not Found: " + username);
            }

            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + "USER");

            User user = new User(username, userDto.password(), Collections.singleton(authority));

            return user;

        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
