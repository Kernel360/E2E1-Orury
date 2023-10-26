package com.kernel360.orury.domain.post.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kernel360.orury.domain.board.db.BoardEntity;

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

	private Long boardId;
	private String postTitle;
	//    @Column(columnDefinition = "TEXT")  // 문찬욱 : 이 어노테이션 안해도 되는지 확실하지가 않네요
	private String postContent;
	private String userNickname;
	private int viewCnt;
	private int likeCnt;
	private boolean isDelete;
	private Long userId;//
	private String createdBy;
	private LocalDateTime createdAt;
	private String updatedBy;
	private LocalDateTime updatedAt;
}
