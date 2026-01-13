package com.CapBackEnd.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FriendResponse {
    private Long id;        // 친구 id
    private String name;    // 친구 이름
    private String email;   // 친구 이메일

    // 핵심 : 친구가 가입한 구독 목록 ( 파티 합류 버튼용 )
    private List<SubscriptionResponse> subscriptions;
}
