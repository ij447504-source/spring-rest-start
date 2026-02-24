package com.metacoding.springv2.reply;

import lombok.Data;

/**
 * 댓글 작성 및 수정을 위한 요청 DTO들을 모아두는 클래스입니다.
 */
public class ReplyRequest {

    /**
     * 댓글 작성을 위한 요청 DTO입니다.
     */
    @Data
    public static class WriteDTO {
        private Integer boardId; // 어떤 게시글에 다는 댓글인지 식별자
        private String comment;  // 댓글 내용
    }
    @Data
    public static class UpdateDTO {
        private String comment;
    }
}
