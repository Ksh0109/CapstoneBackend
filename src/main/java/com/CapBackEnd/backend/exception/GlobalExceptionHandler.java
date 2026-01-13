package com.CapBackEnd.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // 모든 컨트롤러의 에러를 감지함
public class GlobalExceptionHandler {

    // 우리가 의도한 에러 (IllegalArgumentException 등)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 예상치 못한 모든 에러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllException(Exception e) {
        e.printStackTrace(); // 서버 로그에는 에러 표기

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
