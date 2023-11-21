package com.kernel360.orury.domain.post.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
//    Optional<List<PostEntity>> findAllByBoardId(Long boardId);
    List<PostEntity> findAllByBoardId(Long boardId);
}
