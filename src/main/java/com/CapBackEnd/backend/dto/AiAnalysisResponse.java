package com.CapBackEnd.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AiAnalysisResponse {
    private String reportMonth;
    private Integer currentTotal;
    private String status;
    private Double avgUsage;    // 평균 사용 일수

    // 상세 분석 리스트
    private List<AnalyzedSubscription> subscriptions;
    // 해지 후보
    private List<CancelCandidate> cancelCandidates;
    // 요약 멘트
    private String summary;

    @Getter
    @NoArgsConstructor
    public static class AnalyzedSubscription {
        private Long subscriptionId;
        private String serviceName;
        private String category;
        private Integer price;
        private Integer usageDays;
        private String subType; // "OVERSPEND" 등
        private String advice;
    }

    @Getter
    @NoArgsConstructor
    public static class CancelCandidate {
        private Long subscriptionId;
        private String reason;
    }
}
