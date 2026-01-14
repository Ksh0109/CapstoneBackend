package com.CapBackEnd.backend.controller;

import com.CapBackEnd.backend.dto.AuthResponse;
import com.CapBackEnd.backend.dto.UserSettingsRequest;
import com.CapBackEnd.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저 관리 API", description = "내 정보 조회, 설정 변경")
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    @GetMapping("/api/user/me")
    @Operation(summary = "내 정보 조회", description = "토큰 유효성 검사 / 나의 최신 정보 반환")
    public ResponseEntity<AuthResponse.UserDto> getMyInfo(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(userService.getMyInfo(userId));
    }

    // 설정 변경
    @PatchMapping("/api/user/settings")
    @Operation(summary = "유저 설정 변경", description = "닉네임 / 알림 설정 변경")
    public ResponseEntity<AuthResponse.UserDto> updateSettings(@AuthenticationPrincipal Long userId,
                                                               @RequestBody UserSettingsRequest request) {
        return ResponseEntity.ok(userService.updateSettings(userId,request));
    }

}
