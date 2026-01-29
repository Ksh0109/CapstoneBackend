package com.CapBackEnd.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AiAnalysisRequest {
    private Long userId;        // 유저 ID
    private String reportMonth; // "2026-01"
    private List<SubscriptionInfo> subscriptions; // ["Netflix", "Melon"] 이런식으로 이름만 보내기

    @Getter
    @Builder
    public static class SubscriptionInfo {
        private Long id;
        private String serviceName;
        private String category;
        private Integer price;
        private String cycle;
        private Integer usageCount;

        private String startDate;
    }
}
