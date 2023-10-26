package com.kernel360.orury.domain.post.service;

import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.dto.PostDto;

import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class PostConverter {

	public PostDto toDto(PostEntity postEntity) {
		return PostDto.builder()
			.id(postEntity.getId())
			.postTitle(postEntity.getPostTitle())
			.postContent(postEntity.getPostContent())
			.userNickname(postEntity.getUserNickname())
			.viewCnt(postEntity.getViewCnt())
			.likeCnt(postEntity.getLikeCnt())
			.isDelete(postEntity.isDelete())
			.userId(postEntity.getUserId())
			.boardId(postEntity.getBoardId())
			.createdBy(postEntity.getCreatedBy())
			.createdAt(postEntity.getCreatedAt())
			.updatedBy(postEntity.getUpdatedBy())
			.updatedAt(postEntity.getUpdatedAt())
			.build();
	}

	public PostEntity toEntity(PostDto postDto) {
		return PostEntity.builder()
			.id(postDto.getId())
			.postTitle(postDto.getPostTitle())
			.postContent(postDto.getPostContent())
			.userNickname(postDto.getUserNickname())
			.viewCnt(postDto.getViewCnt())
			.likeCnt(postDto.getLikeCnt())
			.isDelete(postDto.isDelete())
			.userId(postDto.getUserId())
			.boardId(postDto.getBoardId())
			.createdBy(postDto.getCreatedBy())
			.createdAt(postDto.getCreatedAt())
			.updatedBy(postDto.getUpdatedBy())
			.updatedAt(postDto.getUpdatedAt())
			.build();
	}
}
