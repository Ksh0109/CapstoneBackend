package com.CapBackEnd.backend.repository;

import com.CapBackEnd.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    // 이메일로 유저 찾기 ( 로그인용 )
    Optional<User> findByEmail(String email);
    // 중복가입 방지용
    boolean existsByEmail(String email);
}
