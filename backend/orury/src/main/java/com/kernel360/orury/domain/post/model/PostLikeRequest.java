package com.kernel360.orury.domain.post.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostLikeRequest {
    @NotNull
    private Long postId;
    @NotNull
    private Long userId;
    @NotNull
    @JsonProperty("is_like")
    private boolean isLike;
}
