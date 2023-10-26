package com.kernel360.orury.domain.post.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.model.PostDto;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostConverter {

	private final BoardRepository boardRepository;

	public PostDto toDto(PostEntity postEntity) {
		return PostDto.builder()
			.id(postEntity.getId())
			.boardId(postEntity.getBoard().getId())
			.postTitle(postEntity.getPostTitle())
			.postContent(postEntity.getPostContent())
			.userNickname(postEntity.getUserNickname())
			.viewCnt(postEntity.getViewCnt())
			.likeCnt(postEntity.getLikeCnt())
			.isDelete(postEntity.isDelete())
			.userId(postEntity.getUserId())
			.createdBy(postEntity.getCreatedBy())
			.createdAt(postEntity.getCreatedAt())
			.updatedBy(postEntity.getUpdatedBy())
			.updatedAt(postEntity.getUpdatedAt())
			.build();
	}

	public PostEntity toEntity(PostDto postDto) {
		BoardEntity boardEntity = boardRepository.findById(postDto.getBoardId())
			.orElseThrow(
				() -> new RuntimeException("해당 게시판이 없습니다: " + postDto.getBoardId())
			);

		return PostEntity.builder()
			.id(postDto.getId())
			.board(boardEntity)
			.postTitle(postDto.getPostTitle())
			.postContent(postDto.getPostContent())
			.userNickname(postDto.getUserNickname())
			.viewCnt(postDto.getViewCnt())
			.likeCnt(postDto.getLikeCnt())
			.isDelete(postDto.isDelete())
			.userId(postDto.getUserId())

			.createdBy(postDto.getCreatedBy())
			.createdAt(postDto.getCreatedAt())
			.updatedBy(postDto.getUpdatedBy())
			.updatedAt(postDto.getUpdatedAt())
			.build();
	}
}
