package com.kernel360.orury.domain.comment.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>{
    // select * from comment where post_id = ?
    List<CommentEntity> findAllByPostIdOrderByIdDesc(Long postId);

    Long countByPostId(Long postId);
}
