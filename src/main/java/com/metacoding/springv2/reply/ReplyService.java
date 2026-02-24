package com.metacoding.springv2.reply;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.handler.ex.Exception403;
import com.metacoding.springv2._core.handler.ex.Exception404;
import com.metacoding.springv2.board.Board;
import com.metacoding.springv2.board.BoardRepository;
import com.metacoding.springv2.user.User;

import lombok.RequiredArgsConstructor;

/**
 * 댓글 서비스 계층입니다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    /**
     * 특정 댓글을 삭제합니다.
     * 
     * @param id            삭제할 댓글 식별자
     * @param sessionUserId 현재 로그인한 사용자 식별자
     */
    @Transactional
    public void 댓글삭제하기(Integer id, Integer sessionUserId) {
        // 1. 댓글 존재 여부 및 작성자 확인 (fetch join으로 user를 한 번에 가져옴)
        Reply reply = replyRepository.mFindByIdWithUser(id)
                .orElseThrow(() -> new Exception404("삭제할 댓글이 존재하지 않습니다"));

        // 2. 권한 확인 (작성자 본인인지 확인)
        if (!reply.getUser().getId().equals(sessionUserId)) {
            throw new Exception403("본인의 댓글만 삭제할 수 있습니다");
        }

        // 3. 댓글 삭제
        replyRepository.deleteById(id);
    }

    /**
     * 새로운 댓글을 작성하고 결과를 반환합니다.
     * 
     * @param reqDTO      댓글 작성 정보 (boardId, comment)
     * @param sessionUser 현재 로그인한 사용자 정보
     * @return 작성된 댓글의 응답 DTO
     * @throws Exception404 게시글이 존재하지 않을 때 발생
     */
    @Transactional
    public ReplyResponse.WriteDTO 댓글쓰기(ReplyRequest.WriteDTO reqDTO, User sessionUser) {
        // 1. 게시글 존재 여부 확인
        Board board = boardRepository.findById(reqDTO.getBoardId())
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));

        // 2. 댓글 엔티티 생성 및 연관관계 설정
        Reply reply = Reply.builder()
                .comment(reqDTO.getComment())
                .user(sessionUser) // 세션에서 가져온 사용자 정보 연결
                .board(board)      // 해당 게시글 연결
                .build();

        // 3. 댓글 저장
        replyRepository.save(reply);

        // 4. 응답 DTO로 변환하여 반환
        return new ReplyResponse.WriteDTO(reply);
    }
    @Transactional
    public ReplyResponse.UpdateDTO 댓글수정하기(Integer id, ReplyRequest.UpdateDTO reqDTO, Integer sessionUserId) {
        Reply reply = replyRepository.mFindByIdWithUser(id)
                .orElseThrow(() -> new Exception404("수정할 댓글이 존재하지 않습니다"));

        if (!reply.getUser().getId().equals(sessionUserId)) {
            throw new Exception403("본인 댓글만 수정할 수 있습니다");
        }

        reply.update(reqDTO.getComment());
        return new ReplyResponse.UpdateDTO(reply);
    }
}
