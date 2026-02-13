package com.CapBackEnd.backend.service;

import com.CapBackEnd.backend.dto.AuthResponse;
import com.CapBackEnd.backend.dto.PasswordChangeRequest;
import com.CapBackEnd.backend.dto.PasswordResetRequest;
import com.CapBackEnd.backend.dto.UserSettingsRequest;
import com.CapBackEnd.backend.entity.Role;
import com.CapBackEnd.backend.entity.Subscription;
import com.CapBackEnd.backend.entity.SubscriptionMember;
import com.CapBackEnd.backend.entity.User;
import com.CapBackEnd.backend.repository.SubscriptionMemberRepository;
import com.CapBackEnd.backend.repository.SubscriptionRepository;
import com.CapBackEnd.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

// 회원 관리용 서비스
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SubscriptionMemberRepository subscriptionMemberRepository;
    private final SubscriptionRepository subscriptionRepository;

    // 내 정보 조회 ( 앱 켤때 /auth/me 호출용 )
    @Transactional
    public AuthResponse.UserDto getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        return AuthResponse.UserDto.builder()
                .id(userId)
                .email(user.getEmail())
                .name(user.getName())
                .isNotifyEnabled(user.isNotifyEnabled())
                .build();
    }

    // 설정 변경 ( PATCH /user/settings )
    @Transactional
    public AuthResponse.UserDto updateSettings(Long userId, UserSettingsRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // Entity에 만들어둔 메서드 사용
        user.updateSettings(request.getIsNotifyEnabled(),request.getName());

        // 변경된 최신 정보 리턴
        return AuthResponse.UserDto.builder()
                .id(userId)
                .email(user.getEmail())
                .name(user.getName())
                .isNotifyEnabled(user.isNotifyEnabled())
                .build();
    }

    public Long findUserIdByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 사용자가 없습니다."));
        return user.getId();
    }


    @Transactional
    public void resetPassword(PasswordResetRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 임시 비밀번호 생성 (8자리로 끊음)
        String tempPassword = UUID.randomUUID().toString().substring(0,8);

        // DB에 비밀번호 덮어쓰기 (Encode 해줘야함)
        user.updatePassword(passwordEncoder.encode(tempPassword));

        // 이메일 전송
        String subject = "[캡스톤] 임시 비밀번호 발급 안내";
        String text = "회원님의 임시 비밀번호는 아래와 같습니다.\n\n" +
                "임시 비밀번호: " + tempPassword + "\n\n" +
                "로그인 후 반드시 비밀번호를 변경해주세요.";

        emailService.sendEmail(user.getEmail(), subject, text);
    }

    @Transactional
    // 비밀번호 변경
    public void changePassword(Long userId, PasswordChangeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    @Transactional
    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 내가 참여 중인 모든 구독 정보 조회
        List<SubscriptionMember> myMemberships = subscriptionMemberRepository.findAllByUser(user);

        // 구독 정보 하나씩 순회하며 정리
        for(SubscriptionMember member : myMemberships) {
            Subscription subscription = member.getSubscription();

            if(member.getRole() == Role.LEADER) {
                // 내가 방장인 경우 -> 파티 폭파

                // 해당 방에 속한 모든 멤버 조회 및 삭제
                List<SubscriptionMember> allMembers = subscriptionMemberRepository.findBySubscription(subscription);
                subscriptionMemberRepository.deleteAll(allMembers);

                // 방 삭제
                subscriptionRepository.delete(subscription);
            }else {
                // 멤버인 경우 -> 나만 탈퇴
                subscriptionMemberRepository.delete(member);
            }

            // 정리 끝나면 데이터 삭제
            userRepository.delete(user);
        }
    }

}
