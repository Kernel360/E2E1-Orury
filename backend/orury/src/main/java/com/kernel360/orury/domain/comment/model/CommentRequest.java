package com.kernel360.orury.domain.comment.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@Builder             // 문찬욱 : superbuilder로?
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentRequest {
    /**
     * create
     * 1. id값이 없으면, 이 request는 본 댓글 작성
     * 2. id값이 있으면, 이 reuqest는 대댓글 작성
     * 2-1. 댓글 row를 생성할 때, p_id 컬럼에 넘어온 id를 꽂아줘야됨.
     * update / delete
     * 1. 수정/삭제는 id의 고유한 값에 대고 수정하는 것이기 때문에 분기처리가 필요없음
     */
    private Long id;
    private Long postId;
    private Long userId;

    @NotBlank
    private String commentContent;

    @NotBlank
    private String userNickname;
}
