package com.CapBackEnd.backend.service;


import com.CapBackEnd.backend.dto.SubscriptionSaveRequest;
import com.CapBackEnd.backend.entity.Role;
import com.CapBackEnd.backend.entity.Subscription;
import com.CapBackEnd.backend.entity.SubscriptionMember;
import com.CapBackEnd.backend.entity.User;
import com.CapBackEnd.backend.repository.SubScriptionMemberRepository;
import com.CapBackEnd.backend.repository.SubscriptionRepository;
import com.CapBackEnd.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubScriptionMemberRepository subscriptionMemberRepository;
    private final UserRepository userRepository;

    // 구독 추가하기 ( 파티 생성 )
    @Transactional
    public void saveSubscription(Long userId, SubscriptionSaveRequest request){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자."));

        // 구독 정보 ( subscription ) 저장
        Subscription subscription = request.toEntity();
        subscriptionRepository.save(subscription);

        // 만드는 사람이 리더가 됨.
        SubscriptionMember member = SubscriptionMember.builder()
                .user(user)
                .subscription(subscription)
                .role(Role.LEADER)
                .build();
        subscriptionMemberRepository.save(member);

    }
    // 내 구독 정보 가져오기
    public List<SubscriptionMember> getMySubscriptions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        return subscriptionMemberRepository.findByUser(user);
    }

}
