package com.CapBackEnd.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 친구 추가 요청용
@Getter
@NoArgsConstructor
public class FriendAddRequest {
    private String email; // 추가할 친구의 이메일
}
