package com.CapBackEnd.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "subscription_members")
public class SubscriptionMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // ★ User랑 연결 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // ★ Subscription이랑 연결 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Enumerated(EnumType.STRING)
    private Role role; // LEADER, MEMBER

    @Column(name = "joined_at")
    @CreationTimestamp // 생성될 때 오늘 날짜 자동 입력
    private LocalDate joinedAt;

    @Builder
    public SubscriptionMember(User user, Subscription subscription, Role role) {
        this.user = user;
        this.subscription = subscription;
        this.role = role;
    }
}
