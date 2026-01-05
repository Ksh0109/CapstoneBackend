package com.CapBackEnd.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    private String password;

    @Column(name = "is_notify_enabled")
    private boolean isNotifyEnabled;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public User(String email, String name, String password, boolean isNotifyEnabled) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.isNotifyEnabled = isNotifyEnabled;
    }
}