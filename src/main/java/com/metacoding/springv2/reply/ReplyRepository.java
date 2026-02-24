package com.metacoding.springv2.reply;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 댓글 엔티티에 대한 데이터베이스 접근 처리를 담당하는 레포지토리입니다.
 */
public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    /**
     * 댓글 ID로 조회 시 작성자(User) 정보를 fetch join하여 한 번에 가져옵니다.
     * 
     * @param id 조회할 댓글 식별자
     * @return 작성자 정보가 포함된 댓글 엔티티 (Optional)
     */
    @Query("select r from Reply r join fetch r.user where r.id = :id")
    Optional<Reply> mFindByIdWithUser(@Param("id") Integer id);
}
