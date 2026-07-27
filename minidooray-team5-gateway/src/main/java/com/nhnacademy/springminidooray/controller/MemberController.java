package com.nhnacademy.springminidooray.controller;

import com.nhnacademy.springminidooray.model.ProjectMemberRequest;
import com.nhnacademy.springminidooray.service.AccountApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Controller
@RequestMapping("/projects")
public class MemberController {
    private final RestTemplate restTemplate;
    private final RedisTemplate redisTemplate;
    private final AccountApiService accountApiService;

    @Value("${api.task.url}")
    private String TASK_URL;
//    private final String TASK_URL = "http://10.201.251.243:8080/api/projects";

    @PostMapping("/{id}/members")
    public String addMembers(@PathVariable String id,
                             @RequestParam String username,
                             @CookieValue("SESSIONID") String sessionId) {

//        String url = TASK_URL + "/" + id + "/members";
        String url = TASK_URL + "/api/projects/" + id + "/members";

        accountApiService.exists(username);

        String adminUser = (String) redisTemplate.opsForValue().get(sessionId);
        ProjectMemberRequest request = new ProjectMemberRequest(adminUser, username);

        restTemplate.postForObject(url, request, String.class);

        return "redirect:/projects/" + id;
    }

    @DeleteMapping("/{id}/members/{username}")
    public String deleteMembers(@PathVariable String id,
                             @PathVariable String username,
                             @CookieValue("SESSIONID") String sessionId) {

        String url = TASK_URL + "/api/projects/" + id + "/members/" + username;

        String adminUser = (String) redisTemplate.opsForValue().get(sessionId);
        ProjectMemberRequest request = new ProjectMemberRequest(adminUser, username);

        HttpEntity<ProjectMemberRequest> httpRequest = new HttpEntity<>(request);

        try {
            restTemplate.exchange(url, HttpMethod.DELETE, httpRequest, Void.class);
        } catch (HttpClientErrorException e) {
            throw e;
        }

        return "redirect:/projects/" + id;
    }
}
