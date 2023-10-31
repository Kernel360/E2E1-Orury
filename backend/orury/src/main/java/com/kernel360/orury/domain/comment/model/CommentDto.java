package com.kernel360.orury.domain.comment.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentDto {
    private Long id;
    private Long userId;
    private String userNickname;
    private Long postId;

    private String commentContent;
    private int likeCnt;

    // 부모 댓글 id, pId == null ? 본댓글 : 대댓글
    private Long pId;

    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
