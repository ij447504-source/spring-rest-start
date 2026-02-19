package com.metacoding.springv2.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final HttpSession session;

    // 필터무력화 체크, 무력화된 경우 이까지 옴

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest.LoginDTO reqDTO) { // json 데이터 파싱

        authService.로그인(reqDTO);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
        return "login까지 오나?";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "health ok";
    }
}
