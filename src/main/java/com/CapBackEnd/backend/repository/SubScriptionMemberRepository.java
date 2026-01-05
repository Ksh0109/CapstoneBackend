package com.CapBackEnd.backend.repository;

import com.CapBackEnd.backend.entity.SubscriptionMember;
import com.CapBackEnd.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubScriptionMemberRepository extends JpaRepository<SubscriptionMember, Long> {
    // 유저가 가입한 내역 전부 가져옴.
    List<SubscriptionMember> findByUser(User user);
}
