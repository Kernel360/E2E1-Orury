package com.kernel360.orury.domain.post.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kernel360.orury.domain.comment.model.CommentDto;
import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
	private String postContent;
	private String userNickname;
	private int viewCnt;
	private int likeCnt;
	private Long userId;//
	private String createdBy;
	private LocalDateTime createdAt;
	private String updatedBy;
	private LocalDateTime updatedAt;
	private String thumbnailUrl;
	private List<String> imageList = List.of();
	private List<CommentDto> commentList = List.of();
	private Map<String, List<CommentDto>> commentMap = Map.of();
	@JsonProperty("is_like")
	private boolean isLike;
}
