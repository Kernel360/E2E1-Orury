package com.kernel360.orury.domain.post.db;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class PostLikePK implements Serializable{
    private Long userId;
    private Long postId;
}