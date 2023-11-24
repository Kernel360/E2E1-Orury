package com.kernel360.orury.domain.board.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.board.model.BoardDto;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.db.PostRepository;
import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.post.service.PostConverter;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardConverter {

	private final PostRepository postRepository;
	private final PostConverter postConverter;

	public BoardDto toDto(BoardEntity boardEntity) {
		List<PostDto> postList = postRepository.findAllByBoardIdOrderByIdDesc(boardEntity.getId())
			.stream()
			.map(postConverter::toDtoOnlyPost)
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

	public BoardDto toDtoOnlyBoard(BoardEntity boardEntity) {
		return BoardDto.builder()
			.id(boardEntity.getId())
			.boardTitle(boardEntity.getBoardTitle())
			.createdBy(boardEntity.getCreatedBy())
			.createdAt(boardEntity.getCreatedAt())
			.updatedBy(boardEntity.getUpdatedBy())
			.updatedAt(boardEntity.getUpdatedAt())
			.postList(List.of())
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
