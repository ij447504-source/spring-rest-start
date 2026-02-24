package com.metacoding.springv2.board;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.metacoding.springv2._core.util.Resp;
import com.metacoding.springv2.user.User;

import lombok.RequiredArgsConstructor;

/**
 * 게시글과 관련된 API 요청을 처리하는 컨트롤러입니다.
 */
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    /**
     * 새로운 게시글을 작성합니다.
     * 
     * @param reqDTO      게시글 작성 정보 (title, content)
     * @param sessionUser 현재 로그인한 사용자 정보 (@AuthenticationPrincipal을 통해 주입)
     * @return 작성 완료된 게시글 상세 정보를 포함한 ResponseEntity
     */
    @PostMapping("/api/boards")
    public ResponseEntity<?> 게시글작성하기(@RequestBody BoardRequest.SaveDTO reqDTO, @AuthenticationPrincipal User sessionUser) {
        // 게시글 서비스를 호출하여 비즈니스 로직 처리
        BoardResponse.DetailDTO respDTO = boardService.게시글작성하기(reqDTO, sessionUser);
        return Resp.ok(respDTO);
    }

    /**
     * 게시글을 삭제합니다.
     * 
     * @param id          삭제할 게시글의 식별자
     * @param sessionUser 현재 로그인한 사용자 정보 (@AuthenticationPrincipal을 통해 주입)
     * @return 삭제 완료 메시지를 포함한 ResponseEntity
     */
    @DeleteMapping("/api/boards/{id}")
    public ResponseEntity<?> 게시글삭제하기(@PathVariable("id") Integer id, @AuthenticationPrincipal User sessionUser) {
        boardService.게시글삭제하기(id, sessionUser.getId());
        return Resp.ok("게시글이 삭제되었습니다");
    }

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

    /**
     * 특정 게시글의 상세 정보를 조회합니다.
     * 
     * @param id 조회할 게시글의 고유 식별자
     * @return 게시글 상세 정보를 포함한 ResponseEntity
     */
    @GetMapping("/api/boards/{id}")
    public ResponseEntity<?> 게시글상세보기(@PathVariable("id") Integer id) {
        BoardResponse.DetailDTO respDTO = boardService.게시글상세보기(id);
        return Resp.ok(respDTO);
    }
}
