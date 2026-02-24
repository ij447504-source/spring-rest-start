package com.metacoding.springv2.board;

import lombok.Data;

/**
 * 게시글 요청과 관련된 DTO들을 모아둔 클래스입니다.
 */
public class BoardRequest {

    /**
     * 게시글 작성을 위한 DTO입니다.
     */
    @Data
    public static class SaveDTO {
        private String title;
        private String content;
    }

    /**
     * 게시글 수정을 위한 DTO입니다.
     */
    @Data
    public static class UpdateDTO {
        private String title;
        private String content;
    }
}
