package com.typespring.example.jwt.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.typespring.example.jwt.dto.SigninDto;
import com.typespring.example.jwt.dto.SignupDto;
import com.typespring.example.jwt.dto.TokenDto;
import com.typespring.example.jwt.dto.TokenReqDto;
import com.typespring.example.jwt.entity.RefreshToken;
import com.typespring.example.jwt.repository.RefreshTokenRepository;
import com.typespring.example.member.Member;
import com.typespring.example.member.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public String signup(SignupDto signupDto) {
        Member member = signupDto.toMember(passwordEncoder);
        Optional<Member> optMember = memberRepository.findByEmail(signupDto.getEmail());
        if (optMember.isPresent()) {
            throw new IllegalArgumentException("email is conflict.");
        }
        return memberRepository.save(member).getEmail();
    }

    @Transactional
    public TokenDto signin(SigninDto signinReqDto) {
        // 1. username, password 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = signinReqDto.toAuthenticationToken();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                                                .key(authentication.getName())
                                                .value(tokenDto.getRefreshToken())
                                                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenReqDto tokenReqDto) throws NotFoundException {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenReqDto.getRefreshToken())) {
            throw new IllegalStateException("invalid refresh token.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenReqDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                                                          .orElseThrow(() -> new NotFoundException());

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenReqDto.getRefreshToken())) {
            throw new IllegalStateException("invalid refresh token.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        refreshToken.updateValue(tokenDto.getRefreshToken());

        // 토큰 발급
        return tokenDto;
    }
}
