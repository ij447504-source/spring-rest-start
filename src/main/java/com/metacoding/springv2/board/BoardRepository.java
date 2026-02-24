package com.metacoding.springv2.board;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 게시글 엔티티에 대한 데이터베이스 접근 처리를 담당하는 레포지토리입니다.
 */
public interface BoardRepository extends JpaRepository<Board, Integer> {

    /**
     * 게시글 ID로 조회 시 작성자(User), 댓글 목록(Replies) 및 댓글 작성자 정보를 fetch join하여 한 번에 가져옵니다.
     * 지연 로딩(Lazy Loading)으로 인한 N+1 문제를 방지합니다.
     * 댓글이 없는 경우에도 게시글이 조회되도록 left join을 사용하며, 중복 결과를 방지하기 위해 distinct를 사용합니다.
     * 
     * @param id 조회할 게시글 식별자
     * @return 작성자 및 댓글 정보가 포함된 게시글 엔티티 (Optional)
     */
    @Query("select distinct b from Board b join fetch b.user left join fetch b.replies r left join fetch r.user where b.id = :id")
    Optional<Board> mFindByIdWithUser(@Param("id") Integer id);
}
