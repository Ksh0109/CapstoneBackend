package com.CapBackEnd.backend.repository;

import com.CapBackEnd.backend.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    // 굳이 추가 안해도 save,findById,delete 등 기본 제공 메서드 가능
}
