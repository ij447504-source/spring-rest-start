package com.metacoding.springv2.reply;

import lombok.Data;

/**
 * 댓글 관련 응답 데이터를 담는 DTO 클래스입니다.
 */
public class ReplyResponse {

    /**
     * 댓글 작성이 완료된 후 반환되는 응답 DTO입니다.
     */
    @Data
    public static class WriteDTO {
        private Integer id;
        private String comment;
        private Integer userId;
        private String username;
        private Integer boardId;

        public WriteDTO(Reply reply) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.userId = reply.getUser().getId();
            this.username = reply.getUser().getUsername();
            this.boardId = reply.getBoard().getId();
        }
    }

    @Data
    public static class UpdateDTO {
        private Integer id;
        private String comment;
        private Integer userId;
        private String username;
        private Integer boardId;

        public UpdateDTO(Reply reply) {
            this.id = reply.getId();
            this.comment = reply.getComment();
            this.userId = reply.getUser().getId();
            this.username = reply.getUser().getUsername();
            this.boardId = reply.getBoard().getId();
        }
    }
}
