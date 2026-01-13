package com.CapBackEnd.backend.service;

import com.CapBackEnd.backend.dto.AuthResponse;
import com.CapBackEnd.backend.dto.UserSettingsRequest;
import com.CapBackEnd.backend.entity.User;
import com.CapBackEnd.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 회원 관리용 서비스
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 내 정보 조회 ( 앱 켤때 /auth/me 호출용 )
    @Transactional
    public AuthResponse.UserDto getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        return AuthResponse.UserDto.builder()
                .id(userId)
                .email(user.getEmail())
                .name(user.getName())
                .isNotifyEnabled(user.isNotifyEnabled())
                .build();
    }

    // 설정 변경 ( PATCH /user/settings )
    @Transactional
    public AuthResponse.UserDto updateSettings(Long userId, UserSettingsRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // Entity에 만들어둔 메서드 사용
        user.updateSettings(request.getIsNotifyEnabled(),request.getName());

        // 변경된 최신 정보 리턴
        return AuthResponse.UserDto.builder()
                .id(userId)
                .email(user.getEmail())
                .name(user.getName())
                .isNotifyEnabled(user.isNotifyEnabled())
                .build();



    }
}
