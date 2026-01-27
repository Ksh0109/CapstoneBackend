package com.CapBackEnd.backend.controller;

import com.CapBackEnd.backend.dto.AiAnalysisResponse;
import com.CapBackEnd.backend.dto.AiChatRequest;
import com.CapBackEnd.backend.dto.AiChatResponse;
import com.CapBackEnd.backend.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/report")
    @Operation(summary = "리포트 조회", description = "없으면 내부적으로 생성 후 반환")
    public ResponseEntity<AiAnalysisResponse> analyze(@AuthenticationPrincipal Long userId) {
        // 수정 예정 : 토큰에서 userId 꺼내는 방식으로
        //Long userId = 1L; // 임시로 1번 유저 하드코딩. 수정됨.

        AiAnalysisResponse response = aiService.getOrGenerateReport(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chat")
    @Operation(summary = "챗봇 메시지 전송")
    public ResponseEntity<Map<String, String>> chat(@AuthenticationPrincipal Long userId,
                                                    @RequestBody AiChatRequest request) {
        String response = aiService.sendChatMessage(userId, request);
        return ResponseEntity.ok(Map.of("response", response));
    }

    // 3. 대화 내역 조회 (GET)
    @GetMapping("/chat/history")
    @Operation(summary = "대화 내역 조회")
    public ResponseEntity<List<AiChatResponse>> getChatHistory(@AuthenticationPrincipal Long userId) {
        List<AiChatResponse> history = aiService.getChatHistory(userId);
        return ResponseEntity.ok(history);
    }

}
