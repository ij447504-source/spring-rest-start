package com.metacoding.springv2.board;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metacoding.springv2._core.util.JwtUtil;
import com.metacoding.springv2.user.User;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @DisplayName("게시글 작성 성공 테스트")
    @Test
    public void save_success_test() throws Exception {
        // given
        BoardRequest.SaveDTO reqDTO = new BoardRequest.SaveDTO();
        reqDTO.setTitle("새 게시글 제목");
        reqDTO.setContent("새 게시글 내용");
        String requestBody = om.writeValueAsString(reqDTO);

        User ssar = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(ssar);

        // when
        ResultActions resultActions = mvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/boards")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value(200));
        resultActions.andExpect(jsonPath("$.body.title").value("새 게시글 제목"));
        resultActions.andExpect(jsonPath("$.body.username").value("ssar"));
        resultActions.andExpect(jsonPath("$.body.userId").value(1));
    }

    @DisplayName("게시글 삭제 성공 테스트")
    @Test
    public void delete_success_test() throws Exception {
        // given
        Integer id = 1; // ssar가 쓴 글
        User ssar = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(ssar);

        // when
        ResultActions resultActions = mvc.perform(
                delete("/api/boards/" + id)
                        .header("Authorization", "Bearer " + jwt));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value(200));
        resultActions.andExpect(jsonPath("$.msg").value("성공"));
        resultActions.andExpect(jsonPath("$.body").value("게시글이 삭제되었습니다"));
    }

    @DisplayName("게시글 삭제 권한 실패 테스트 (본인이 아님)")
    @Test
    public void delete_fail_forbidden_test() throws Exception {
        // given
        Integer id = 1; // ssar가 쓴 글
        User cos = User.builder().id(2).username("cos").roles("USER").build();
        String jwt = JwtUtil.create(cos);

        // when
        ResultActions resultActions = mvc.perform(
                delete("/api/boards/" + id)
                        .header("Authorization", "Bearer " + jwt));

        // then
        resultActions.andExpect(status().isForbidden()); // 403
        resultActions.andExpect(jsonPath("$.status").value(403));
        resultActions.andExpect(jsonPath("$.msg").value("본인의 게시글만 삭제할 수 있습니다"));
    }

    @DisplayName("게시글 삭제 존재하지 않음 실패 테스트")
    @Test
    public void delete_fail_not_found_test() throws Exception {
        // given
        Integer id = 100; // 존재하지 않는 글
        User ssar = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(ssar);

        // when
        ResultActions resultActions = mvc.perform(
                delete("/api/boards/" + id)
                        .header("Authorization", "Bearer " + jwt));

        // then
        resultActions.andExpect(status().isNotFound()); // 404
        resultActions.andExpect(jsonPath("$.status").value(404));
        resultActions.andExpect(jsonPath("$.msg").value("삭제할 게시글이 존재하지 않습니다"));
    }

    @DisplayName("게시글 상세보기 성공 테스트 (댓글 포함)")
    @Test
    public void detail_success_test() throws Exception {
        // given
        Integer id = 4; // data.sql 기준 댓글이 3개 있는 게시글
        User ssar = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(ssar);

        // when
        ResultActions resultActions = mvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/boards/" + id)
                        .header("Authorization", "Bearer " + jwt));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value(200));
        resultActions.andExpect(jsonPath("$.body.id").value(4));
        resultActions.andExpect(jsonPath("$.body.replies").isArray());
        resultActions.andExpect(jsonPath("$.body.replies.length()").value(3));
        resultActions.andExpect(jsonPath("$.body.replies[0].comment").value("comment1"));
        resultActions.andExpect(jsonPath("$.body.replies[0].username").value("cos"));
    }
}
