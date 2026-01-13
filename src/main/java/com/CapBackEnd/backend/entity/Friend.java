package com.CapBackEnd.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friends", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"from_user_id", "to_user_id"}) // 중복 친구 추가 방지
})
public class Friend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;  // 나

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser; // 친구

    @Builder
    public Friend(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

}
