package com.metacoding.springv2.board;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2._core.util.Resp;

import lombok.RequiredArgsConstructor;

/**
 * 게시글과 관련된 API 요청을 처리하는 컨트롤러입니다.
 */
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 목록을 조회하여 반환합니다.
     * 
     * @return 게시글 목록 DTO 리스트를 포함한 ResponseEntity
     */
    @GetMapping("/api/boards")
    public ResponseEntity<?> 게시글목록보기() {
        List<BoardResponse.ListDTO> respDTOs = boardService.게시글목록보기();
        return Resp.ok(respDTOs);
    }
}
