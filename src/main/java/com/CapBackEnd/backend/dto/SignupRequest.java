package com.CapBackEnd.backend.dto;

import com.CapBackEnd.backend.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
    private String name;

    public User toEntity(PasswordEncoder passwordEncoder){
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .isNotifyEnabled(true)
                .build();
    }
}
