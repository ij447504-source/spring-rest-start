package com.metacoding.springv2.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.util.JwtUtil;
import com.metacoding.springv2.user.User;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("로그아웃 성공 테스트")
    @Test
    public void logout_success_test() throws Exception {
        // given
        User ssar = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(ssar);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/logout")
                        .header("Authorization", "Bearer " + jwt));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value(200));
        resultActions.andExpect(jsonPath("$.msg").value("성공"));
        resultActions.andExpect(jsonPath("$.body").value("로그아웃 되었습니다"));
    }

    @DisplayName("로그아웃 인증 실패 테스트")
    @Test
    public void logout_fail_unauthorized_test() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(post("/api/logout"));

        // then
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.status").value(401));
    }
}
