package com.nhnacademy.springminidooray.controller;

import com.nhnacademy.springminidooray.model.dto.SignupRequest;
import com.nhnacademy.springminidooray.service.AccountApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class SignupController {
    private final AccountApiService accountApiService;

    @GetMapping("/signup")
    public String signUpForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String doSignUp(@ModelAttribute SignupRequest request) {
        accountApiService.createUser(request);

        return "redirect:/projects";
    }
}
