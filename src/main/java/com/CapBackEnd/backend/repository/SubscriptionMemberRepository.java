package com.CapBackEnd.backend.repository;

import com.CapBackEnd.backend.entity.Subscription;
import com.CapBackEnd.backend.entity.SubscriptionMember;
import com.CapBackEnd.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionMemberRepository extends JpaRepository<SubscriptionMember, Long> {
    // 특정 구독 방의 모든 멤버 조회 ( 파티원 목록 )
    List<SubscriptionMember> findBySubscription(Subscription subscription);
    // 특정 유저의 구독 목록 조회
    List<SubscriptionMember> findByUser(User user);
    // 유저 ID와 구독 방 번호로 내 명단 찾기
    Optional<SubscriptionMember> findByUserAndSubscriptionId(Long userId, Long subscriptionId);
    // 중복 가입 방지용 ( 이미 이 방에 들어왔는지 확인 )
    boolean existsByUserAndSubscription(User user, Subscription subscription);
}
