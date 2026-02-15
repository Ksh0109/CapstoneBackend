package com.CapBackEnd.backend.service;

import com.CapBackEnd.backend.dto.AuthResponse;
import com.CapBackEnd.backend.dto.LoginRequest;
import com.CapBackEnd.backend.dto.SignupRequest;
import com.CapBackEnd.backend.dto.SocialLoginRequest;
import com.CapBackEnd.backend.entity.User;
import com.CapBackEnd.backend.jwt.JwtTokenProvider;
import com.CapBackEnd.backend.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    @Value("${google.client-id}")
    private String googleClientId;

    // 회원가입
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        // 이메일 인증 여부 확인
        if(!emailService.isVerified(request.getEmail())) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }
        // 이메일 중복 검사
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다");
        }
        // 유저 저장 ( pw 암호화는 DTO 안에서 함 )
        User user = userRepository.save(request.toEntity(passwordEncoder));

        // 인증 상태 제거
        emailService.removeVerified(request.getEmail());

        // 가입 성공하면 바로 로그인 처리
        String token = jwtTokenProvider.createToken(user.getId());
        return AuthResponse.of(token, user);
    }

    // 로그인
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request){
        // 이메일 확인
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        // 비밀번호 확인 (암호화된 비번이랑 비교)
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        // 토큰 발급
        String token = jwtTokenProvider.createToken(user.getId());
        return AuthResponse.of(token, user);
    }

    // 소셜 로그인
    @Transactional
    public AuthResponse socialLogin(SocialLoginRequest request) {
        String email = "";
        String name = "";

        // 플랫폼별 토큰 검증 및 정보 추출
        if ("google".equalsIgnoreCase(request.getProvider())) {
            GoogleIdToken.Payload payload = verifyGoogleToken(request.getToken());
            email = payload.getEmail();
            name = (String) payload.get("name");
        }else{
            throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다.");
        }

        String finalEmail = email;
        String finalName = name;
        User user = userRepository.findByEmail(finalEmail)
                .orElseGet(() -> userRepository.save(User.builder()
                            .email(finalEmail)
                            .name(finalName != null ? finalName : "소셜유저")    // 이름 없으면 기본값
                            // 소셜 유저는 비번이 없으니까 랜덤값으로 설정 ( 일반 로그인 불가 )
                            .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                            .isNotifyEnabled(true)
                            .build()
                    )
                );
        // 서비스 전용 JWT 토큰 발급
        String serviceToken = jwtTokenProvider.createToken(user.getId());

        // 응답 반환
        return AuthResponse.of(serviceToken, user);
    }

    // 구글 ID Token 검증해주는 메서드
    private GoogleIdToken.Payload verifyGoogleToken(String idTokenString) {
        try {
            // 내 클라이언트 ID 맞는지 확인
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();
            // 토큰 검증 수행
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if(idToken != null) {
                return idToken.getPayload(); // 이메일,이름 등 정보 리턴
            }else {
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("구글 로그인 실패: " + e.getMessage());
        }
    }
}
