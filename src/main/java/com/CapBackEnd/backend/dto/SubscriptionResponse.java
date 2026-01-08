package com.CapBackEnd.backend.dto;

import com.CapBackEnd.backend.entity.Role;
import com.CapBackEnd.backend.entity.SubscriptionMember;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class SubscriptionResponse {
    private Long id;          // 구독 ID
    private String name;      // 구독 서비스 명
    private Integer price;
    private String payDate;
    private String cycle;
    private String themeColor;

    private Boolean isLeader;
    private List<MemberDto> members;

    @Getter
    @Builder
    public static class MemberDto {
        private Long id;
        private String name;
    }

    // Entity -> DTO 변환
    public static SubscriptionResponse of(SubscriptionMember myMember, List<SubscriptionMember> allMembers) {
        boolean isLeader = myMember.getRole() == Role.LEADER;

        // 내가 리더면 전체 멤버 보여주고, 아니면 빈 리스트
        List<MemberDto> memberDtos = isLeader ?
                allMembers.stream()
                        .map(m -> MemberDto.builder()
                                .id(m.getUser().getId())
                                .name(m.getUser().getName())
                                .build())
                        .collect(Collectors.toList())
                : List.of();

        return SubscriptionResponse.builder()
                .id(myMember.getSubscription().getId())
                .name(myMember.getSubscription().getName())
                .price(myMember.getSubscription().getPrice())
                .cycle(myMember.getSubscription().getCycle())
                .payDate(myMember.getSubscription().getPayDate())
                .themeColor(myMember.getSubscription().getThemeColor())
                .isLeader(isLeader)
                .members(memberDtos)
                .build();
    }
}