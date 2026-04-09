package com.CapBackEnd.backend.repository;

import com.CapBackEnd.backend.entity.Friend;
import com.CapBackEnd.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend,Long> {
    // 내가 추가한 친구 목록 조회
    List<Friend> findAllByFromUser(User fromUser);

    // 이미 친구인지 확인용
    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    // 계정 삭제 시 양방향 친구 관계 정리용
    List<Friend> findAllByToUser(User toUser);
}