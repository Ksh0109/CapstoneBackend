package com.CapBackEnd.backend.controller;

import com.CapBackEnd.backend.dto.AuthResponse;
import com.CapBackEnd.backend.dto.LoginRequest;
import com.CapBackEnd.backend.dto.PasswordResetRequest;
import com.CapBackEnd.backend.dto.SignupRequest;
import com.CapBackEnd.backend.service.AuthService;
import com.CapBackEnd.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "로그인, 회원가입")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입 성공 시 토큰과 유저 정보를 반환")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일/비번으로 로그인하고 토큰 발급받음.")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // 임시 비밀번호 발급
    @PostMapping("/password/reset")
    @Operation(summary = "임시 비밀번호 발급", description = "입력한 이메일로 임시 비밀번호 발송됨.")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok(Map.of("message","이메일로 임시 비밀번호가 전송되었습니다."));
    }

    @DeleteMapping("/account")
    @Operation(summary = "계정 삭제", description = "가입된 파티에서 모두 탈퇴되고 계정 삭제")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal Long userId){
        userService.deleteAccount(userId);
        return ResponseEntity.ok(Map.of("message","계정이 삭제되었습니다."));
    }

}
