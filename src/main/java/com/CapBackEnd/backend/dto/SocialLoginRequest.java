package com.CapBackEnd.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialLoginRequest {
    private String provider;    // "google" 등 소셜 이름
    private String token;       // Access Token / ID Token
}
