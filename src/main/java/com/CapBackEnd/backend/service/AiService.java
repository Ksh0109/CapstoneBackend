package com.CapBackEnd.backend.service;

import com.CapBackEnd.backend.client.AiServiceClient;
import com.CapBackEnd.backend.dto.AiAnalysisRequest;
import com.CapBackEnd.backend.dto.AiAnalysisResponse;
import com.CapBackEnd.backend.entity.AiReport;
import com.CapBackEnd.backend.entity.SubscriptionMember;
import com.CapBackEnd.backend.entity.User;
import com.CapBackEnd.backend.repository.AiReportRepository;
import com.CapBackEnd.backend.repository.SubscriptionMemberRepository;
import com.CapBackEnd.backend.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiService {

    private final AiServiceClient aiServiceClient; // 전화기
    private final UserRepository userRepository;
    private final SubscriptionMemberRepository subscriptionMemberRepository;
    private final AiReportRepository aiReportRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public AiAnalysisResponse runAnalysis(Long userId) {
        // 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 해당 유저의 구독 목록 싹 긁어오기
        List<SubscriptionMember> members = subscriptionMemberRepository.findByUser(user);

        // 구독 정보에서 필요한 정보만 추출.
        List<String> subNames = members.stream()
                .map(m -> m.getSubscription().getName())
                .collect(Collectors.toList());

        // 파이썬에게 전송할 Request 작성
        AiAnalysisRequest request = AiAnalysisRequest.builder()
                .userId(userId)
                .targetMonth(LocalDate.now().toString().substring(0, 7))
                .subscriptionNames(subNames)
                .build();

        // 파이썬 서버 호출
        AiAnalysisResponse response = aiServiceClient.analyzeSubscription(request);

        // 결과 DB에 저장
        String categoryJson = "[]";
        try {
            categoryJson = objectMapper.writeValueAsString(response.getCategoryList());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        AiReport report = AiReport.builder()
                .user(user)
                .reportMonth(response.getReportMonth())
                .currentTotal(response.getCurrentTotal())
                .score(response.getScore())
                .status(response.getStatus())
                .predictedTotal(response.getPredictedTotal())
                .predictionComment(response.getPredictionComment())
                .summary(response.getSummary())
                .advice(response.getAdvice())
                .categoryJson(categoryJson) // 변환된 JSON 문자열 저장
                .build();

        aiReportRepository.save(report);

        // 결과 리턴
        return response;
    }

}
