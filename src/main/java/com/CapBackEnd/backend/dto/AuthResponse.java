package com.CapBackEnd.backend.dto;

import com.CapBackEnd.backend.entity.User;
import lombok.Builder;
import lombok.Getter;


// 응답 : 토큰 + 유저 정보

@Getter
@Builder
public class AuthResponse {
    private String token;
    private UserDto user;

    @Getter
    @Builder
    public static class UserDto {
        private Long id;
        private String email;
        private String name;
        private boolean isNotifyEnabled;
    }

    public static AuthResponse of(String token, User user) {
        return AuthResponse.builder()
                .token(token)
                .user(UserDto.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .isNotifyEnabled(user.isNotifyEnabled())
                        .build())
                .build();
    }

}
