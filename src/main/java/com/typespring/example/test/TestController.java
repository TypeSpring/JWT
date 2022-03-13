package com.typespring.example.test;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.typespring.example.util.SecurityUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping
    public String get() throws IllegalAccessException {
//        log.info("[TEST] Email = {}", email);
        final String currentMemberEmail = SecurityUtil.getCurrentMemberEmail();
        return "[GET] " + currentMemberEmail;
    }

    @PostMapping
    public String post() throws IllegalAccessException {
        final String currentMemberEmail = SecurityUtil.getCurrentMemberEmail();
        return "[POST] " + currentMemberEmail;
    }

    @PutMapping
    public String put() throws IllegalAccessException {
        final String currentMemberEmail = SecurityUtil.getCurrentMemberEmail();
        return "[PUT] " + currentMemberEmail;
    }

    @DeleteMapping
    public String delete() throws IllegalAccessException {
        final String currentMemberEmail = SecurityUtil.getCurrentMemberEmail();
        return "[DELETE] " + currentMemberEmail;
    }
}
