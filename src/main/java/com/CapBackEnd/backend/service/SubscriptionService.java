package com.CapBackEnd.backend.service;


import com.CapBackEnd.backend.dto.SubscriptionResponse;
import com.CapBackEnd.backend.dto.SubscriptionSaveRequest;
import com.CapBackEnd.backend.entity.Role;
import com.CapBackEnd.backend.entity.Subscription;
import com.CapBackEnd.backend.entity.SubscriptionMember;
import com.CapBackEnd.backend.entity.User;
import com.CapBackEnd.backend.repository.SubscriptionMemberRepository;
import com.CapBackEnd.backend.repository.SubscriptionRepository;
import com.CapBackEnd.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMemberRepository subscriptionMemberRepository;
    private final UserRepository userRepository;

    // 구독 추가하기 ( 파티 생성 or 합류 )
    @Transactional
    public void saveSubscription(Long userId, SubscriptionSaveRequest request){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자."));

        // 구독 정보 ( subscription ) 저장
        Subscription subscription;
        Role role;

        // 경우 1 : 파티 합류 ( leaderId가 있음 )
        if(request.getLeaderId() != null) {
        // 일단 내가 만들면 무조건 리더인걸로 하고, 나중에 고도화
            subscription = request.toEntity();
            subscriptionRepository.save(subscription);
            role = Role.LEADER;
        }else {
            // 경우 2 : 신규 생성 ( 내가 방장 )
            subscription = request.toEntity();
            subscriptionRepository.save(subscription);
            role = Role.LEADER;
        }

        SubscriptionMember member = SubscriptionMember.builder()
                .user(user)
                .subscription(subscription)
                .role(role)
                .build();

        subscriptionMemberRepository.save(member);

    }
    // 내 구독 정보 가져오기 ( DTO 조립 )
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getMySubscriptions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 1. 내가 가입한 멤버 정보들 가져옴
        List<SubscriptionMember> myMembers = subscriptionMemberRepository.findByUser(user);
        List<SubscriptionResponse> responses = new ArrayList<>();

        for(SubscriptionMember myMember : myMembers) {
            // 2. 이 구독 방의 모든 멤버 가져옴
            List<SubscriptionMember> partyMembers = subscriptionMemberRepository
                    .findBySubscription(myMember.getSubscription());
            // 3. DTO 조립
            responses.add(SubscriptionResponse.of(myMember, partyMembers));
        }

        return responses;
    }

    // 구독 정보 수정 ( PATCH )
    @Transactional
    public void updateSubscription(Long userId, Long subscriptionId, SubscriptionSaveRequest request) {
        // 1. 내가 이 방의 멤버인지 확인
        SubscriptionMember member = subscriptionMemberRepository.findByUserAndSubscriptionId(userId, subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("가입된 정보가 없습니다."));

        // 2. 구독 정보 가져오기
        Subscription subscription = member.getSubscription();

        // 3. 정보 업데이트 (방장만 가능한지 체크하는 로직은 나중에 추가 가능)
        subscription.update(request.getPrice(), request.getPayDate(), request.getThemeColor());
    }
    // 구독 삭제 / 파티 탈퇴 (DELETE)
    @Transactional
    public void deleteSubscription(Long userId, Long subscriptionId) {
        SubscriptionMember member = subscriptionMemberRepository.findByUserAndSubscriptionId(userId, subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("가입된 정보가 없습니다."));

        Subscription subscription = member.getSubscription();

        // 내가 리더인지 판단
        if (member.getRole() == Role.LEADER) {
            // 리더면 -> 파티 전체 폭파
            // (연관된 모든 멤버를 먼저 삭제해야 깔끔하게 지워짐)
            List<SubscriptionMember> allMembers = subscriptionMemberRepository.findBySubscription(subscription);
            subscriptionMemberRepository.deleteAll(allMembers);
            // 파티 방 삭제
            subscriptionRepository.delete(subscription);
        } else {
            // 멤버면 -> 나만 탈퇴 (내 명단만 삭제)
            subscriptionMemberRepository.delete(member);
        }
    }
}
