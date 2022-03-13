package com.typespring.example.jwt.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.typespring.example.member.Member;
import com.typespring.example.member.Member.Authority;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignupDto {
    private String email;
    private String password;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                     .email(email)
                     .authority(Authority.ROLE_USER)
                     .password(passwordEncoder.encode(password))
                     .build();
    }
}
