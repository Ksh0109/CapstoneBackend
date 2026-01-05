package com.CapBackEnd.backend.client;

import com.CapBackEnd.backend.dto.AiAnalysisRequest;
import com.CapBackEnd.backend.dto.AiAnalysisResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service", url="$(ai.server.url}")
public interface AiServiceClient {
    @PostMapping("/api/v1/anaylze")
    AiAnalysisResponse analyzeSubscription(@RequestBody AiAnalysisRequest request);
}
