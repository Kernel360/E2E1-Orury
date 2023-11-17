package com.kernel360.orury.domain.post.db;

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
@Entity(name = "post_like")
@EntityListeners(Listener.class)
@Data
public class PostLikeEntity extends BaseEntity {
    @EmbeddedId
    private PostLikePK postLikePK;

    // userId에 접근하는 메서드 추가
    public Long getUserId() {
        return this.postLikePK.getUserId();
    }
}
