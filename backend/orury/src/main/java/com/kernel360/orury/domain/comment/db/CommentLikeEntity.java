package com.kernel360.orury.domain.comment.db;

import com.kernel360.orury.global.common.BaseEntity;
import com.kernel360.orury.global.common.Listener;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Entity(name = "comment_like")
@EntityListeners(Listener.class)
@Data
public class CommentLikeEntity extends BaseEntity {
    @EmbeddedId
    private CommentLikePK commentLikePK;

    // userId에 접근하는 메서드 추가
    public Long getUserId() {
        return this.commentLikePK.getUserId();
    }

    // commentId에 접근하는 메서드 추가
    public Long getCommentId() {
        return this.commentLikePK.getCommentId();
    }
}
