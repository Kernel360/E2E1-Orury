package com.kernel360.orury.domain.post.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostDto {
    private Long id;
    private String postTitle;
//    @Column(columnDefinition = "TEXT")  // 문찬욱 : 이 어노테이션 안해도 되는지 확실하지가 않네요
    private String postContent;
    private String userNickname;
    private int viewCnt;
    private int likeCnt;
    private int isDelete;
    private Long userId;
    private Long boardId;
}
