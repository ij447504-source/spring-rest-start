package com.metacoding.springv2.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.metacoding.springv2._core.handler.ex.Exception401;
import com.metacoding.springv2.auth.AuthRequest.LoginDTO;
import com.metacoding.springv2.user.User;
import com.metacoding.springv2.user.UserRepository;
import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void 로그인(LoginDTO reqDTO) {
        // 1. UserRepositiory에서 username, password 확인
        User findUser = userRepository.findByUsername(reqDTO.getUsername())
                .orElseThrow(() -> new Exception401("유저네임을 찾을 수 없어요"));
        // ssar이 있는지만 찾기 ,아직 비밀번호인증안한상태

        // 2. password를 hash해서 비교검증
        boolean isSamepassword = bCryptPasswordEncoder.matches(reqDTO.getPassword(), findUser.getPassword());
        // 해쉬안된것과 해쉬된 것을 매칭

        if (!isSamepassword)
            throw new Exception401("비밀번호가 틀렸어요");
        // 이런 코드로 간단하게 가능, 인터셉트 등 만들지 않아도 됨

        // 3. Authentication 객체 만들기
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                findUser,
                null,
                findUser.getAuthorities());

        // 4. SecurityContextHolder에 저장 (Authentication 객체 넣기)
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
