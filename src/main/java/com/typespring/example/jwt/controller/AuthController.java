package com.typespring.example.jwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.typespring.example.jwt.dto.SigninDto;
import com.typespring.example.jwt.dto.SignupDto;
import com.typespring.example.jwt.dto.TokenDto;
import com.typespring.example.jwt.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupDto signupDto) {
        log.info("[Signup Request] {}", signupDto);
        String email = authService.signup(signupDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(email + "====> 회원가입이 완료되었습니다.");
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signin(@RequestBody SigninDto signinDto) {
        log.info("[Signin Request] {}", signinDto);
        TokenDto tokenDto = authService.signin(signinDto);
        return ResponseEntity.ok(tokenDto);
    }

}
