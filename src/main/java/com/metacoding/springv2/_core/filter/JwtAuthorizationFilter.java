package com.metacoding.springv2._core.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.metacoding.springv2._core.util.JwtProvider;
import com.metacoding.springv2._core.util.JwtUtil;
import com.metacoding.springv2.user.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 인가 필터
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // localhost:8080/api/good (모든주소에서 동작하는 필터)
        // header -> Authorization : Bearer JWT토큰

        String jwt = JwtProvider.토큰추출하기(request);

        if (jwt != null) { // 토큰이 널이 아닐때만 인증객체만들기
            Authentication authentication = JwtProvider.인증객체만들기(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}