package com.kernel360.orury.domain.comment.db;

import com.kernel360.orury.domain.post.db.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, Long> {
    CommentLikeEntity findByCommentLikePKUserIdAndCommentLikePKCommentId(long userId, long commentId);

    Long countByCommentLikePKCommentId(Long commentId);
}
