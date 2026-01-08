package com.CapBackEnd.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AiAnalysisResponse {
    private String reportMonth;

    private Integer currentTotal;
    private Integer score;
    private String status;

    private Integer predictedTotal;
    private String predictionComment;

    private String summary;
    private String advice;

    // 파이썬이 리스트로 주면, 자바가 받아서 나중에 문자열로 변환할 예정
    private List<CategoryData> categoryList;

    // 내부 클래스로 리스트 모양 정의
    @Getter
    @NoArgsConstructor
    public static class CategoryData {
        private String name;
        private Integer amount;
        private Integer percent;
    }
}
