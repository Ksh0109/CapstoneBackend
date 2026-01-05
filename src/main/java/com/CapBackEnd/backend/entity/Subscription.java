package com.CapBackEnd.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 서비스명

    @Column(nullable = false)
    private Integer price; // 금액

    private String cycle; // 결제 주기

    @Column(name = "pay_date")
    private String payDate; // 결제일

    @Column(name = "theme_color")
    private String themeColor; // 테마 색상

    @Column(name = "image_url")
    private String imageUrl; // 로고 이미지 URL

    @Builder
    public Subscription(String name, Integer price, String cycle, String payDate, String themeColor, String imageUrl) {
        this.name = name;
        this.price = price;
        this.cycle = cycle;
        this.payDate = payDate;
        this.themeColor = themeColor;
        this.imageUrl = imageUrl;
    }
}
