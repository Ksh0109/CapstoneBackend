package com.CapBackEnd.backend.service;

import com.CapBackEnd.backend.client.AiServiceClient;
import com.CapBackEnd.backend.dto.AiAnalysisRequest;
import com.CapBackEnd.backend.dto.AiAnalysisResponse;
import com.CapBackEnd.backend.dto.AiChatRequest;
import com.CapBackEnd.backend.dto.AiChatResponse;
import com.CapBackEnd.backend.entity.AiChatMessage;
import com.CapBackEnd.backend.entity.AiReport;
import com.CapBackEnd.backend.entity.SubscriptionMember;
import com.CapBackEnd.backend.entity.User;
import com.CapBackEnd.backend.repository.AiChatMessageRepository;
import com.CapBackEnd.backend.repository.AiReportRepository;
import com.CapBackEnd.backend.repository.SubscriptionMemberRepository;
import com.CapBackEnd.backend.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiService {

    private final AiServiceClient aiServiceClient; // 전화기
    private final UserRepository userRepository;
    private final SubscriptionMemberRepository subscriptionMemberRepository;
    private final AiReportRepository aiReportRepository;
    private final AiChatMessageRepository aiChatMessageRepository;
    private final ObjectMapper objectMapper;

    // 리포트 조회 ( 없으면 생성 )
    @Transactional
    public AiAnalysisResponse getOrGenerateReport(Long userId) {
        // 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // 이번달 리포트가 이미 DB에 있는지 확인
        Optional<AiReport> existingReport = aiReportRepository.findByUserAndReportMonth(user,currentMonth);

        if(existingReport.isPresent()) {
            //이미 있으면 DB에서 꺼내서 DTO로 변환 후 반환
            return convertEntityToDto(existingReport.get());
        }

        // 없으면 파이썬 서버에 분석 요청

        // 해당 유저의 구독 목록 싹 긁어오기
        List<SubscriptionMember> members = subscriptionMemberRepository.findByUser(user);

        // 요청 DTO 생성
        List<AiAnalysisRequest.SubscriptionInfo> subInfos = members.stream()
                .map(m -> AiAnalysisRequest.SubscriptionInfo.builder()
                        .id(m.getSubscription().getId())
                        .serviceName(m.getSubscription().getName())
                        .category("OTT") // 수정 필요 ( Subscription 엔티티에 필드 추가하거나 이름 넣기 )
                        .price(m.getSubscription().getPrice())
                        .cycle(m.getSubscription().getCycle())
                        .usageCount(3) // 수정 필요 ( 실제 사용 횟수로 )
                        .startDate(m.getJoinedAt().toString())
                        .build())
                .collect(Collectors.toList());

        // 파이썬으로 전송할 Request 작성
        AiAnalysisRequest request = AiAnalysisRequest.builder()
                .userId(userId)
                .reportMonth(currentMonth)
                .subscriptions(subInfos)
                .build();
                // 수정 예정

        // 파이썬 서버 호출
        AiAnalysisResponse response = aiServiceClient.analyzeSubscription(request);

        // 결과 DB에 저장
        String categoryJson = "[]";
        try {
            categoryJson = objectMapper.writeValueAsString(response);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        AiReport report = AiReport.builder()
                .user(user)
                .reportMonth(response.getReportMonth())
                .currentTotal(response.getCurrentTotal())
                .status(response.getStatus())
                .avgUsage(response.getAvgUsage())
                .summary(response.getSummary())
                .analysisResultJson(categoryJson)
                .build();

        aiReportRepository.save(report);

        // 결과 리턴
        return response;
    }

    // 챗봇 메시지 전송
    @Transactional
    public String sendChatMessage(Long userId, AiChatRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 파이썬 서버로 질문 전송
        // 파이썬 응답이 단순 String이라고 가정
        String aiReply = aiServiceClient.sendChatMessage(request);

        // 대화 내용 DB 저장
        AiChatMessage chatMessage = AiChatMessage.builder()
                .user(user)
                .userMessage(request.getMessage())
                .aiResponse(aiReply)
                .build();

        aiChatMessageRepository.save(chatMessage);

        return aiReply;
    }

    // 3. 대화 내역 조회
    @Transactional(readOnly = true)
    public List<AiChatResponse> getChatHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        List<AiChatMessage> messages = aiChatMessageRepository.findAllByUserOrderByCreatedAtAsc(user);

        // 화면에 뿌려주기 좋게 변환 (User 메시지와 AI 메시지를 분리해서 리스트화)
        List<AiChatResponse> history = new ArrayList<>();

        for (AiChatMessage msg : messages) {
            // 유저 말
            history.add(AiChatResponse.builder()
                    .role("user")
                    .message(msg.getUserMessage())
                    .timestamp(msg.getCreatedAt())
                    .build());
            // AI 말
            history.add(AiChatResponse.builder()
                    .role("assistant")
                    .message(msg.getAiResponse())
                    .timestamp(msg.getCreatedAt()) // 같은 시간 or 약간 뒤
                    .build());
        }

        return history;
    }

    private AiAnalysisResponse convertEntityToDto(AiReport entity) {
        try {
            // JSON 문자열을 다시 DTO 객체로 복원
            return objectMapper.readValue(entity.getAnalysisResultJson(), AiAnalysisResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("리포트 복원 실패", e);
        }
    }

}
