package com.CapBackEnd.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    // 인증 코드 저장 (email -> CodeEntry)
    private final ConcurrentHashMap<String, CodeEntry> codeStore = new ConcurrentHashMap<>();
    // 인증 완료된 이메일
    private final Set<String> verifiedEmails = ConcurrentHashMap.newKeySet();

    private static final int CODE_LENGTH = 6;
    private static final int EXPIRY_MINUTES = 3;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            javaMailSender.send(message);
            System.out.println("✅ 이메일 전송 성공: " + to);
        } catch (Exception e) {
            System.err.println("❌ 이메일 전송 실패: " + e.getMessage());
            throw new RuntimeException("이메일 전송 중 오류가 발생했습니다.");
        }
    }

    // 인증 코드 생성 및 이메일 발송
    public void sendVerificationCode(String email) {
        String code = generateCode();
        codeStore.put(email, new CodeEntry(code, LocalDateTime.now().plusMinutes(EXPIRY_MINUTES)));
        verifiedEmails.remove(email);

        String subject = "[구독 관리 서비스] 이메일 인증 코드";
        String text = "인증 코드: " + code + "\n\n이 코드는 " + EXPIRY_MINUTES + "분간 유효합니다.";
        sendEmail(email, subject, text);
    }

    // 인증 코드 검증
    public void verifyCode(String email, String code) {
        CodeEntry entry = codeStore.get(email);
        if (entry == null) {
            throw new IllegalArgumentException("인증 코드를 먼저 요청해주세요.");
        }
        if (LocalDateTime.now().isAfter(entry.expiresAt)) {
            codeStore.remove(email);
            throw new IllegalArgumentException("인증 코드가 만료되었습니다. 다시 요청해주세요.");
        }
        if (!entry.code.equals(code)) {
            throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
        }
        codeStore.remove(email);
        verifiedEmails.add(email);
    }

    // 이메일 인증 완료 여부 확인
    public boolean isVerified(String email) {
        return verifiedEmails.contains(email);
    }

    // 인증 상태 제거 (회원가입 완료 후 호출)
    public void removeVerified(String email) {
        verifiedEmails.remove(email);
    }

    private String generateCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // 100000 ~ 999999
        return String.valueOf(code);
    }

    private record CodeEntry(String code, LocalDateTime expiresAt) {}
}
