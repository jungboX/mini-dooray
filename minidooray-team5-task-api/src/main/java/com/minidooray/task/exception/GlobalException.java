package com.minidooray.task.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler({Exception.class})
    public ResponseEntity globalException() {
        return ResponseEntity.status(404).build();
    }
}
