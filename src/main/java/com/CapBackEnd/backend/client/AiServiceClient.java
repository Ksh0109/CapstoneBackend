package com.CapBackEnd.backend.client;

import com.CapBackEnd.backend.dto.AiAnalysisRequest;
import com.CapBackEnd.backend.dto.AiAnalysisResponse;
import com.CapBackEnd.backend.dto.AiChatRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service", url="${ai.server.url}")
public interface AiServiceClient {
    // 분석 요청
    @PostMapping("/api/v1/analyze")
    AiAnalysisResponse analyzeSubscription(@RequestBody AiAnalysisRequest request);
    // 챗봇 대화 요청
    @PostMapping("/api/v1/chat")
    String sendChatMessage(@RequestBody AiChatRequest request);
    // 파이썬 쪽 응답이 String 인지 JSON 인지 확인.
    // 일단은 {"reply" : ".."} 형식으로 가정함.
}
