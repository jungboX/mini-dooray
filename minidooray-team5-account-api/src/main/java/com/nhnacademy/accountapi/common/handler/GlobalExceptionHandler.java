package com.nhnacademy.accountapi.common.handler;

import com.nhnacademy.accountapi.common.dto.ErrorResponse;
import com.nhnacademy.accountapi.user.exception.UnauthorizedException;
import com.nhnacademy.accountapi.user.exception.UserAlreadyExistsException;
import com.nhnacademy.accountapi.user.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotUserFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            "USER_NOT_FOUND",
            ex.getMessage()
        );
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<ErrorResponse> handleUnAuthorizedException(UnauthorizedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "UNAUTHORIZED",
                ex.getMessage()
        );
        return ResponseEntity.status(401).body(errorResponse);
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            "USER_ALREADY_EXISTS",
            ex.getMessage()
        );
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            "INTERNAL_SERVER_ERROR",
            ex.getMessage()
        );
        return ResponseEntity.status(500).body(errorResponse);
    }
}
