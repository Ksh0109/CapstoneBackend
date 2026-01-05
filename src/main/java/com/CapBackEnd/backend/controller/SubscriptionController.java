package com.CapBackEnd.backend.controller;


import com.CapBackEnd.backend.dto.SubscriptionResponse;
import com.CapBackEnd.backend.dto.SubscriptionSaveRequest;
import com.CapBackEnd.backend.entity.SubscriptionMember;
import com.CapBackEnd.backend.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // 구독 추가 POST 요청
    @PostMapping
    public ResponseEntity<String> createSubscription(@RequestBody SubscriptionSaveRequest request){
        // 나중에 토큰에서 userId 꺼내서 사용해야함
        Long userId = 1L;

        subscriptionService.saveSubscription(userId,request);
        return ResponseEntity.ok("구독이 추가되었습니다.");
    }
    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getMySubscriptions() {
        // 여기도 나중에 수행
        Long userId = 1L;

        List<SubscriptionMember> members = subscriptionService.getMySubscriptions(userId);

        // 받아온 entity -> dto 변환
        List<SubscriptionResponse> response = members.stream()
                .map(SubscriptionResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}
