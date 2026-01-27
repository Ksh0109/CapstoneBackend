package com.CapBackEnd.backend.repository;

import com.CapBackEnd.backend.entity.AiChatMessage;
import com.CapBackEnd.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiChatMessageRepository extends JpaRepository<AiChatMessage, Long> {
    List<AiChatMessage> findAllByUserOrderByCreatedAtAsc(User user);
}
