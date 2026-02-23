package com.metacoding.springv2.board;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 게시글 엔티티에 대한 데이터베이스 접근 처리를 담당하는 레포지토리입니다.
 */
public interface BoardRepository extends JpaRepository<Board, Integer> {
    
}
