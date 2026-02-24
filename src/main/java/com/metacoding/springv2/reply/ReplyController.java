package com.metacoding.springv2.reply;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2._core.util.Resp;
import com.metacoding.springv2.user.User;

import lombok.RequiredArgsConstructor;

/**
 * 댓글 관련 API 요청을 처리하는 컨트롤러입니다.
 */
@RequiredArgsConstructor
@RestController
public class ReplyController {

    private final ReplyService replyService;

    /**
     * 특정 댓글을 삭제합니다.
     * 
     * @param id          삭제할 댓글 식별자
     * @param sessionUser 현재 로그인한 사용자 정보 (@AuthenticationPrincipal을 통해 주입)
     * @return 삭제 완료 메시지를 포함한 ResponseEntity
     */
    @DeleteMapping("/api/replies/{id}")
    public ResponseEntity<?> 댓글삭제하기(@PathVariable("id") Integer id, @AuthenticationPrincipal User sessionUser) {
        replyService.댓글삭제하기(id, sessionUser.getId());
        return Resp.ok("댓글이 삭제되었습니다");
    }

    /**
     * 특정 게시글에 댓글을 작성합니다.
     * 
     * @param reqDTO      댓글 작성 정보 (boardId, comment)
     * @param sessionUser 현재 로그인한 사용자 정보 (@AuthenticationPrincipal을 통해 주입)
     * @return 작성된 댓글 정보를 포함한 ResponseEntity
     */
    @PostMapping("/api/replies")
    public ResponseEntity<?> 댓글쓰기(@RequestBody ReplyRequest.WriteDTO reqDTO,
            @AuthenticationPrincipal User sessionUser) {
        // 댓글 서비스를 호출하여 비즈니스 로직 처리
        ReplyResponse.WriteDTO respDTO = replyService.댓글쓰기(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }
    @PutMapping("/api/replies/{id}")
    public ResponseEntity<?> 댓글수정하기(@PathVariable("id") Integer id, @RequestBody ReplyRequest.UpdateDTO reqDTO,
            @AuthenticationPrincipal User sessionUser) {
        ReplyResponse.UpdateDTO respDTO = replyService.댓글수정하기(id, reqDTO, sessionUser.getId());
        return Resp.ok(respDTO);
    }
}
