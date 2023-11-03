package com.kernel360.orury.domain.board.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.model.BoardDto;
import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.post.service.PostConverter;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class BoardConverter {

	private final PostConverter postConverter;

	public BoardDto toDto(BoardEntity boardEntity) {
		var postList = boardEntity.getPostList()
			.stream()
			.map(postEntity -> {
				PostDto postDto = postConverter.toDto(postEntity);
//				postDto.setImageList(Collections.emptyList()); // 빈 리스트로 설정
				return postDto;
			})
			.toList();

		return BoardDto.builder()
			.id(boardEntity.getId())
			.boardTitle(boardEntity.getBoardTitle())
			.createdBy(boardEntity.getCreatedBy())
			.createdAt(boardEntity.getCreatedAt())
			.updatedBy(boardEntity.getUpdatedBy())
			.updatedAt(boardEntity.getUpdatedAt())
			.postList(postList)
			.build();
	}

	public BoardEntity toEntity(BoardDto boardDto) {
		return BoardEntity.builder()
			.id(boardDto.getId())
			.boardTitle(boardDto.getBoardTitle())
			.createdAt(boardDto.getCreatedAt())
			.createdBy(boardDto.getCreatedBy())
			.updatedAt(boardDto.getUpdatedAt())
			.updatedBy(boardDto.getUpdatedBy())
			.build();
	}
}
