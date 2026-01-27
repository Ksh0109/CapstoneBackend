package com.CapBackEnd.backend.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AiChatResponse {
    private String role; // 사용자 or AI Agent
    private String message;
    private LocalDateTime timestamp;
}