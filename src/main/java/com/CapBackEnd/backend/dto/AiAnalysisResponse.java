package com.CapBackEnd.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiAnalysisResponse {
    private Integer score;      // 점수
    private String comment;     // "무슨 구독 해지하세요" 이런식으로 코멘트
    private String status;
}
