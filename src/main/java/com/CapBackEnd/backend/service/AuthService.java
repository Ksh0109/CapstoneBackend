package com.CapBackEnd.backend.service;

import com.CapBackEnd.backend.dto.AuthResponse;
import com.CapBackEnd.backend.dto.LoginRequest;
import com.CapBackEnd.backend.dto.SignupRequest;
import com.CapBackEnd.backend.entity.User;
import com.CapBackEnd.backend.jwt.JwtTokenProvider;
import com.CapBackEnd.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        // 이메일 중복 검사
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다");
        }
        // 유저 저장 ( pw 암호화는 DTO 안에서 함 )
        User user = userRepository.save(request.toEntity(passwordEncoder));

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


}
