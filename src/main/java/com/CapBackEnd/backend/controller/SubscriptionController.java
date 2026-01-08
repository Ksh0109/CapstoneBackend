package com.CapBackEnd.backend.controller;


import com.CapBackEnd.backend.dto.SubscriptionResponse;
import com.CapBackEnd.backend.dto.SubscriptionSaveRequest;
import com.CapBackEnd.backend.entity.SubscriptionMember;
import com.CapBackEnd.backend.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@Tag(name = "구독 관리 API", description = "구독 추가, 조회, 수정, 삭제")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // 내 구독 목록 조회
    @GetMapping
    @Operation(summary = "내 구독 목록 조회", description = "내가 가입한 파티 목록 리턴. (본인이 리더인 경우 isLeader=true")
    public ResponseEntity<List<SubscriptionResponse>> getMySubscriptions() {
        // 임시로 하드코딩. Security 적용 후 수정 필요.
        Long userId = 1L;

        List<SubscriptionResponse> response = subscriptionService.getMySubscriptions(userId);
        return ResponseEntity.ok(response);
    }

    // 구독 추가 POST 요청
    @PostMapping
    @Operation(summary = "구독 추가 / 파티 합류", description = "새로운 구독을 생성하거나(leaderId 없음), 친구 파티에 합류(leaderId 있음)합니다.")
    public ResponseEntity<Void> saveSubscription(@RequestBody SubscriptionSaveRequest request) {
        // 임시로 하드코딩. Security 적용 후 수정
        Long userId = 1L;

        subscriptionService.saveSubscription(userId, request);

        // 아직은 빈 응답만 리턴함.
        return ResponseEntity.ok().build();
    }

    // 구독 정보 수정
    @PatchMapping("/{id}")
    @Operation(summary = "구독 정보 수정", description = "가격, 결제일, 색상 등을 수정합니다.")
    public ResponseEntity<Void> updateSubscription(@PathVariable Long id, @RequestBody SubscriptionSaveRequest request) {
        Long userId = 1L; // 임시 유저
        subscriptionService.updateSubscription(userId, id, request);
        return ResponseEntity.ok().build();
    }

    // 구독 삭제 (파티 탈퇴/폭파)
    // DELETE /api/subscriptions/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "구독 삭제/탈퇴", description = "내가 방장이면 파티 전체 삭제, 멤버면 나만 탈퇴합니다.")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        Long userId = 1L; // 임시 유저
        subscriptionService.deleteSubscription(userId, id);
        return ResponseEntity.ok().build();
    }

}
