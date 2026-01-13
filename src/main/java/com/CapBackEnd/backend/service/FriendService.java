package com.CapBackEnd.backend.service;

import com.CapBackEnd.backend.dto.FriendAddRequest;
import com.CapBackEnd.backend.dto.FriendResponse;
import com.CapBackEnd.backend.dto.SubscriptionResponse;
import com.CapBackEnd.backend.entity.Friend;
import com.CapBackEnd.backend.entity.User;
import com.CapBackEnd.backend.repository.FriendRepository;
import com.CapBackEnd.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;  // 친구 구독 목록 가져오기 위해 사용

    // 친구 추가
    @Transactional
    public FriendResponse addFriend(Long userId, FriendAddRequest request) {
        User me = userRepository.findById(userId).orElseThrow();

        // 이메일로 친구 찾기
        User friendUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 유저가 없습니다."));

        // 유효성 검사 ( 나 자신 추가 X, 이미 친구일때도 X )
        if (me.getId().equals(friendUser.getId())) {
            throw new IllegalArgumentException("자기 자신은 친구로 추가할 수 없습니다.");
        }
        if (friendRepository.existsByFromUserAndToUser(me,friendUser)){
            throw new IllegalArgumentException("이미 추가된 친구입니다.");
        }

        // 저장
        Friend friend = Friend.builder()
                .fromUser(me)
                .toUser(friendUser)
                .build();
        friendRepository.save(friend);

        return getFriendResponse(friendUser);


    }

    // 친구 목록 조회
    @Transactional(readOnly = true)
    public List<FriendResponse> getMyFriends(Long userId) {
        User me = userRepository.findById(userId).orElseThrow();
        List<Friend> friends = friendRepository.findAllByFromUser(me);

        // 친구들 정보를 하나씩 DTO로 변환
        return friends.stream()
                .map(friend -> getFriendResponse(friend.getToUser()))
                .collect(Collectors.toList());
    }

    // 3. 친구 삭제
    @Transactional
    public void deleteFriend(Long userId, Long friendId) {
        // friendId는 친구 목록의 PK가 아니라 친구 유저의 ID임.

        User me = userRepository.findById(userId).orElseThrow();
        User friendUser = userRepository.findById(friendId).orElseThrow();

        Friend friendship = friendRepository.findAllByFromUser(me).stream()
                .filter(f -> f.getToUser().getId().equals(friendId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("친구 관계가 아닙니다."));

        friendRepository.delete(friendship);
    }


    // 공통 로직 : 유저 정보 -> FriendResponse 로 변환 ( 구독 목록 포함 )
    private FriendResponse getFriendResponse(User friendUser){
        // 친구의 구독 목록 조회
        List<SubscriptionResponse> subList = subscriptionService.getMySubscriptions(friendUser.getId());

        return FriendResponse.builder()
                .id(friendUser.getId())
                .name(friendUser.getName())
                .email(friendUser.getEmail())
                .subscriptions(subList) // 친구 구독 리스트 들어가있음
                .build();

    }


}
