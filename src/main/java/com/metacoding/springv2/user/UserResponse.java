package com.metacoding.springv2.user;

import java.sql.Timestamp;

import lombok.Data;

public class UserResponse {

    /**
     * 회원정보보기 상세보기 DTO
     */
    @Data
    public static class DetailDTO {
        private Integer id;
        private String username;
        private String email;
        private Timestamp createdAt;

        public DetailDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.createdAt = user.getCreatedAt();
        }
    }
}
