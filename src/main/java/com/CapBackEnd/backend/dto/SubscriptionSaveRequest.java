package com.CapBackEnd.backend.dto;

import com.CapBackEnd.backend.entity.Subscription;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubscriptionSaveRequest {
    private String name;      // 넷플릭스
    private Integer price;    // 17000
    private String cycle;     // monthly
    private String payDate;   // "25"
    private String themeColor;// "#E50914"

    private Long leaderId;

    public Subscription toEntity() {
        return Subscription.builder()
                .name(name)
                .price(price)
                .payDate(payDate)
                .cycle(cycle)
                .themeColor(themeColor)
                .build();
    }

}
