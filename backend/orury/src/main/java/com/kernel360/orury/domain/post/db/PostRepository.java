package com.kernel360.orury.domain.post.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
//    Optional<List<PostEntity>> findAllByBoardIdOrderByIdDesc(Long boardId);
//    List<PostEntity> findAllByBoardIdOrderByIdDesc(Long boardId);

//    @Query(value = "SELECT * FROM Post WHERE board_id = :boardId", nativeQuery = true)
    List<PostEntity> findAllByBoardIdOrderByIdDesc(Long boardId);
}
