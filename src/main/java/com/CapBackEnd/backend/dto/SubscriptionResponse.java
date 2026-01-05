package com.CapBackEnd.backend.dto;

import com.CapBackEnd.backend.entity.Role;
import com.CapBackEnd.backend.entity.SubscriptionMember;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscriptionResponse {
    private Long id;          // 구독 ID
    private String name;      // 구독 서비스 명
    private Integer price;
    private String payDate;
    private String cycle;
    private String themeColor;
    private Role role;        // LEADER (내가 리더인지 확인용)

    public static SubscriptionResponse from(SubscriptionMember member) {
        return SubscriptionResponse.builder()
                .id(member.getSubscription().getId())
                .name(member.getSubscription().getName())
                .price(member.getSubscription().getPrice())
                .payDate(member.getSubscription().getPayDate())
                .cycle(member.getSubscription().getCycle())
                .themeColor(member.getSubscription().getThemeColor())
                .role(member.getRole())
                .build();
    }
}