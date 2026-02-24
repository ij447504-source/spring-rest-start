package com.metacoding.springv2.board;

import java.util.List;
import java.util.stream.Collectors;

import com.metacoding.springv2.reply.Reply;

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

    /**
     * 게시글 상세보기를 위한 DTO입니다.
     * 작성자의 정보를 별도의 객체로 분리하지 않고 userId와 username으로 직접 포함합니다.
     * 게시글에 달린 댓글 목록을 포함합니다.
     */
    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private String username;
        private List<ReplyDTO> replies;

        public DetailDTO(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.userId = board.getUser().getId();
            this.username = board.getUser().getUsername();
            // rule1.md의 지침에 따라 람다식을 명시적으로 사용하여 댓글 목록을 변환합니다.
            this.replies = board.getReplies().stream()
                    .map(reply -> new ReplyDTO(reply))
                    .collect(Collectors.toList());
        }

        /**
         * 게시글 상세보기 시 함께 보여줄 댓글 정보를 위한 DTO입니다.
         */
        @Data
        public static class ReplyDTO {
            private Integer id;
            private String comment;
            private Integer userId;
            private String username;

            public ReplyDTO(Reply reply) {
                this.id = reply.getId();
                this.comment = reply.getComment();
                this.userId = reply.getUser().getId();
                this.username = reply.getUser().getUsername();
            }
        }
    }
}
