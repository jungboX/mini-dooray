package com.nhnacademy.springminidooray.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.net.ConnectException;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 연결 실패 및 HTTP 오류(4xx, 5xx) 처리
    @ExceptionHandler({
        ConnectException.class,
        HttpClientErrorException.class
    })
    public String handleHttpClientErrorException(Exception e, HttpServletRequest request) {
        // 이전 페이지로 리다이렉트
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
