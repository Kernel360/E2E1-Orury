package com.kernel360.orury.domain.post.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {
    PostLikeEntity findByPostLikePKUserIdAndPostLikePKPostId(long userId, long postId);

    Long countByPostLikePKPostId(long postId);
}
