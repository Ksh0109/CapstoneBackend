package com.CapBackEnd.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSettingsRequest {
    private String name;                // 변경할 닉네임
    private Boolean isNotifyEnabled;    // 변경할 알림 설정
}
