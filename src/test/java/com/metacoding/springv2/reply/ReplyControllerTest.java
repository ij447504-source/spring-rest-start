package com.metacoding.springv2.reply;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class ReplyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @DisplayName("댓글 삭제 성공 테스트")
    @Test
    public void delete_success_test() throws Exception {
        // given
        Integer id = 1; // cos가 작성한 1번 댓글
        User cos = User.builder().id(2).username("cos").roles("USER").build();
        String jwt = JwtUtil.create(cos);

        // when
        ResultActions resultActions = mvc.perform(
                delete("/api/replies/" + id)
                        .header("Authorization", "Bearer " + jwt));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value(200));
        resultActions.andExpect(jsonPath("$.msg").value("성공"));
        resultActions.andExpect(jsonPath("$.body").value("댓글이 삭제되었습니다"));
    }

    @DisplayName("댓글 삭제 권한 실패 테스트 (본인이 아님)")
    @Test
    public void delete_fail_forbidden_test() throws Exception {
        // given
        Integer id = 1; // cos가 작성한 1번 댓글
        User ssar = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(ssar);

        // when
        ResultActions resultActions = mvc.perform(
                delete("/api/replies/" + id)
                        .header("Authorization", "Bearer " + jwt));

        // then
        resultActions.andExpect(status().isForbidden()); // 403
        resultActions.andExpect(jsonPath("$.status").value(403));
        resultActions.andExpect(jsonPath("$.msg").value("본인의 댓글만 삭제할 수 있습니다"));
    }

    @DisplayName("댓글 쓰기 성공 테스트")
    @Test
    public void write_success_test() throws Exception {
        // given
        ReplyRequest.WriteDTO reqDTO = new ReplyRequest.WriteDTO();
        reqDTO.setBoardId(1);
        reqDTO.setComment("댓글 테스트입니다");
        String requestBody = om.writeValueAsString(reqDTO);

        User ssar = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(ssar);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/replies")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value(200));
        resultActions.andExpect(jsonPath("$.msg").value("성공"));
        resultActions.andExpect(jsonPath("$.body.comment").value("댓글 테스트입니다"));
        resultActions.andExpect(jsonPath("$.body.userId").value(1));
        resultActions.andExpect(jsonPath("$.body.username").value("ssar"));
        resultActions.andExpect(jsonPath("$.body.boardId").value(1));
    }

    @DisplayName("댓글 쓰기 실패 테스트 - 존재하지 않는 게시글")
    @Test
    public void write_fail_not_found_test() throws Exception {
        // given
        ReplyRequest.WriteDTO reqDTO = new ReplyRequest.WriteDTO();
        reqDTO.setBoardId(100); // 존재하지 않는 게시글
        reqDTO.setComment("댓글 테스트입니다");
        String requestBody = om.writeValueAsString(reqDTO);

        User ssar = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(ssar);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/replies")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isNotFound()); // 404
        resultActions.andExpect(jsonPath("$.status").value(404));
        resultActions.andExpect(jsonPath("$.msg").value("게시글을 찾을 수 없습니다"));
    }
    @DisplayName("댓글 수정 성공 테스트")
    @Test
    public void update_success_test() throws Exception {
        // given
        Integer id = 1; // cos가 작성한 1번 댓글
        ReplyRequest.UpdateDTO reqDTO = new ReplyRequest.UpdateDTO();
        reqDTO.setComment("댓글 수정 테스트입니다");
        String requestBody = om.writeValueAsString(reqDTO);

        User cos = User.builder().id(2).username("cos").roles("USER").build();
        String jwt = JwtUtil.create(cos);

        // when
        ResultActions resultActions = mvc.perform(
                put("/api/replies/" + id)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value(200));
        resultActions.andExpect(jsonPath("$.msg").value("성공"));
        resultActions.andExpect(jsonPath("$.body.id").value(1));
        resultActions.andExpect(jsonPath("$.body.comment").value("댓글 수정 테스트입니다"));
        resultActions.andExpect(jsonPath("$.body.userId").value(2));
        resultActions.andExpect(jsonPath("$.body.username").value("cos"));
        resultActions.andExpect(jsonPath("$.body.boardId").value(4));
    }

    @DisplayName("댓글 수정 권한 실패 테스트 (본인 아님)")
    @Test
    public void update_fail_forbidden_test() throws Exception {
        // given
        Integer id = 1; // cos가 작성한 1번 댓글
        ReplyRequest.UpdateDTO reqDTO = new ReplyRequest.UpdateDTO();
        reqDTO.setComment("댓글 수정 테스트입니다");
        String requestBody = om.writeValueAsString(reqDTO);

        User ssar = User.builder().id(1).username("ssar").roles("USER").build();
        String jwt = JwtUtil.create(ssar);

        // when
        ResultActions resultActions = mvc.perform(
                put("/api/replies/" + id)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isForbidden());
        resultActions.andExpect(jsonPath("$.status").value(403));
        resultActions.andExpect(jsonPath("$.msg").value("본인 댓글만 수정할 수 있습니다"));
    }

    @DisplayName("댓글 수정 실패 테스트 - 존재하지 않는 댓글")
    @Test
    public void update_fail_not_found_test() throws Exception {
        // given
        Integer id = 100; // 존재하지 않는 댓글
        ReplyRequest.UpdateDTO reqDTO = new ReplyRequest.UpdateDTO();
        reqDTO.setComment("댓글 수정 테스트입니다");
        String requestBody = om.writeValueAsString(reqDTO);

        User cos = User.builder().id(2).username("cos").roles("USER").build();
        String jwt = JwtUtil.create(cos);

        // when
        ResultActions resultActions = mvc.perform(
                put("/api/replies/" + id)
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.status").value(404));
        resultActions.andExpect(jsonPath("$.msg").value("수정할 댓글이 존재하지 않습니다"));
    }
}
