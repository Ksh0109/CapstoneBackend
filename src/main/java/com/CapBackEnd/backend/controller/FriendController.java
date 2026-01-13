package com.CapBackEnd.backend.controller;

import com.CapBackEnd.backend.dto.FriendAddRequest;
import com.CapBackEnd.backend.dto.FriendResponse;
import com.CapBackEnd.backend.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@Tag(name = "친구 관리 API", description = "친구 추가, 목록 조회 ( 구독 공유 )")
public class FriendController {
    private final FriendService friendService;

    @GetMapping
    @Operation(summary = "친구 목록 조회", description = "내 친구 목록과 그들의 구독 정보를 반환합니다.")
    public ResponseEntity<List<FriendResponse>> getFriends(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(friendService.getMyFriends(userId));
    }

    @PostMapping
    @Operation(summary = "친구 추가", description = "이메일로 친구를 검색해 추가합니다.")
    public ResponseEntity<FriendResponse> addFriend(@AuthenticationPrincipal Long userId,
                                                    @RequestBody FriendAddRequest request) {
        return ResponseEntity.ok(friendService.addFriend(userId, request));
    }

    @DeleteMapping("/{friendId}")
    @Operation(summary = "친구 삭제", description = "친구 목록에서 삭제합니다.")
    public ResponseEntity<Void> deleteFriend(@AuthenticationPrincipal Long userId,
                                             @PathVariable Long friendId) {
        friendService.deleteFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }
}
