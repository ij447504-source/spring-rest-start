package com.metacoding.springv2.board;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.metacoding.springv2._core.handler.ex.Exception403;
import com.metacoding.springv2._core.handler.ex.Exception404;
import com.metacoding.springv2.user.User;

import lombok.RequiredArgsConstructor;

/**
 * 게시글에 대한 비즈니스 로직을 처리하는 서비스 계층입니다.
 * 모든 메서드는 기본적으로 읽기 전용 트랜잭션 내에서 실행됩니다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 특정 게시글의 상세 정보를 조회하여 DTO로 변환하여 반환합니다.
     * fetch join을 사용하는 레포지토리 메서드를 호출하여 N+1 문제를 해결합니다.
     * 
     * @param id 조회할 게시글의 식별자
     * @return 리팩토링된 게시글 상세 정보 DTO
     * @throws Exception404 게시글을 찾을 수 없는 경우 발생
     */
    public BoardResponse.DetailDTO 게시글상세보기(Integer id) {
        // fetch join을 사용하는 mFindByIdWithUser 메서드를 호출합니다.
        Board board = boardRepository.mFindByIdWithUser(id)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));
        return new BoardResponse.DetailDTO(board);
    }

    /**
     * 새로운 게시글을 작성하고 결과를 반환합니다.
     * 
     * @param reqDTO      게시글 작성 정보 (title, content)
     * @param sessionUser 현재 로그인한 사용자 정보
     * @return 작성된 게시글의 상세 정보 DTO
     */
    @Transactional
    public BoardResponse.DetailDTO 게시글작성하기(BoardRequest.SaveDTO reqDTO, User sessionUser) {
        // 1. 게시글 엔티티 생성 및 연관관계 설정
        Board board = Board.builder()
                .title(reqDTO.getTitle())
                .content(reqDTO.getContent())
                .user(sessionUser) // 현재 세션 유저와 연결
                .build();

        // 2. 게시글 저장
        boardRepository.save(board);

        // 3. 응답 DTO로 변환하여 반환 (작성 직후에는 댓글이 없으므로 DetailDTO 생성자가 안전하게 처리함)
        return new BoardResponse.DetailDTO(board);
    }

    /**
     * 게시글을 삭제합니다.
     * 작성자 본인만 삭제할 수 있도록 권한을 확인합니다.
     * 
     * @param id            삭제할 게시글 식별자
     * @param sessionUserId 현재 로그인한 사용자의 식별자
     */
    @Transactional
    public void 게시글삭제하기(Integer id, Integer sessionUserId) {
        // 1. 게시글 존재 여부 및 작성자 확인 (fetch join으로 user를 한 번에 가져옴)
        Board board = boardRepository.mFindByIdWithUser(id)
                .orElseThrow(() -> new Exception404("삭제할 게시글이 존재하지 않습니다"));

        // 2. 권한 확인 (작성자 본인인지 확인)
        if (!board.getUser().getId().equals(sessionUserId)) {
            throw new Exception403("본인의 게시글만 삭제할 수 있습니다");
        }

        // 3. 게시글 삭제 (게시글이 삭제되면 댓글도 CASCADE 설정에 의해 자동 삭제됩니다)
        boardRepository.deleteById(id);
    }

    /**
     * 모든 게시글 목록을 조회하여 DTO로 변환하여 반환합니다.
     * rule1.md의 규칙에 따라 스트림의 람다식을 명시적으로 작성합니다.
     * 
     * @return 게시글 목록 DTO 리스트
     */
    public List<BoardResponse.ListDTO> 게시글목록보기() {
        List<Board> boardList = boardRepository.findAll();
        // 람다식을 사용하여 엔티티를 DTO로 변환합니다.
        return boardList.stream()
                .map(board -> new BoardResponse.ListDTO(board))
                .collect(Collectors.toList());
    }
}
