package com.CapBackEnd.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AiAnalysisRequest {
    private Long userId;        // 유저 ID
    private String targetMonth; // "2026-01"
    private List<String> subscriptionNames; // ["Netflix", "Melon"] 이런식으로 이름만 보내기
}
