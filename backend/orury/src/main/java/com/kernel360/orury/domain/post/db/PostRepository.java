package com.kernel360.orury.domain.post.repository;

import com.kernel360.orury.domain.post.db.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Optional<PostEntity> findByIdAndIsDelete(Long id, int isDeleted);
}
