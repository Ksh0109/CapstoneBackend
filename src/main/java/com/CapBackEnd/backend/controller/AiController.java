package com.CapBackEnd.backend.controller;

import com.CapBackEnd.backend.dto.AiAnalysisResponse;
import com.CapBackEnd.backend.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/analyze")
    public ResponseEntity<AiAnalysisResponse> analyze(@AuthenticationPrincipal Long userId) {
        // 수정 예정 : 토큰에서 userId 꺼내는 방식으로
        //Long userId = 1L; // 임시로 1번 유저 하드코딩. 수정됨.

        AiAnalysisResponse response = aiService.runAnalysis(userId);
        return ResponseEntity.ok(response);
    }

}
