package com.kernel360.orury.domain.comment.db;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class CommentLikePK implements Serializable {
    private Long commentId;
    private Long userId;
}
