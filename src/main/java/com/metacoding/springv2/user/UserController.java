package com.metacoding.springv2.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2._core.util.Resp;

import lombok.RequiredArgsConstructor;

/**
 * 사용자 정보와 관련된 API 요청을 처리하는 컨트롤러입니다.
 */
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 특정 사용자의 상세 정보를 조회합니다.
     * 
     * @param id 조회할 사용자의 고유 식별자
     * @return 사용자의 상세 정보를 포함한 응답 객체
     */
    @GetMapping("/api/users/{id}")
    public ResponseEntity<?> 회원정보보기(@PathVariable Integer id) {
        UserResponse.DetailDTO respDTO = userService.회원정보보기(id);
        return Resp.ok(respDTO);
    }
}
