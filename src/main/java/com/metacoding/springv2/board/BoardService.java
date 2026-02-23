package com.metacoding.springv2.board;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * 게시글에 대한 비즈니스 로직을 처리하는 서비스 계층입니다.
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 모든 게시글 목록을 조회하여 DTO로 변환하여 반환합니다.
     * 
     * @return 게시글 목록 DTO 리스트
     */
    public List<BoardResponse.ListDTO> 게시글목록보기() {
        List<Board> boardList = boardRepository.findAll();
        return boardList.stream().map(board -> new BoardResponse.ListDTO(board)).collect(Collectors.toList());
    }
}
