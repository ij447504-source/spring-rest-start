package com.metacoding.springv2.board;

import lombok.Data;

/**
 * 게시글과 관련된 응답 데이터를 담는 클래스입니다.
 */
public class BoardResponse {

    /**
     * 게시글 목록 조회를 위한 DTO입니다.
     * 게시글의 식별자(id), 제목(title), 내용(content)을 포함합니다.
     */
    @Data
    public static class ListDTO {
        private Integer id;
        private String title;
        private String content;

        public ListDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
        }
    }
}
