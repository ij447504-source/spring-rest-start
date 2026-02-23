package com.metacoding.springv2.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import com.metacoding.springv2._core.util.Resp;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody AuthRequest.JoinDTO reqDTO) {
        var respDTO = authService.회원가입(reqDTO);
        return Resp.ok(respDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest.LoginDTO reqDTO) {
        String accessToken = authService.로그인(reqDTO);
        return Resp.ok(accessToken);
    }

    /**
     * 유저네임 중복체크
     * 
     * @param username
     * @return
     */
    @GetMapping("/check")
    public ResponseEntity<?> check(@RequestParam("username") String username) {
        authService.유저네임중복체크(username);
        return Resp.ok("사용가능한 유저네임입니다");
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "health ok";
    }
}
