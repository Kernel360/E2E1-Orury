package com.kernel360.orury.domain.board.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.board.model.BoardDto;
import com.kernel360.orury.domain.board.model.BoardRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

	public final BoardRepository boardRepository;
	public final BoardConverter boardConverter;

	public BoardDto createBoard(
		BoardRequest boardRequest
	) {
		var entity = BoardEntity.builder()
			.boardTitle(boardRequest.getBoardTitle())
			.createdBy("admin")  // 임의로 "admin" 넣음
			.createdAt(LocalDateTime.now())
			.updatedBy("admin") // 임의로 "admin" 넣음
			.updatedAt(LocalDateTime.now())
			.build();

		var saveEntity = boardRepository.save(entity);

		return boardConverter.toDto(saveEntity);
	}

	// 게시판 조회
	public List<BoardDto> getBoard() {
		List<BoardEntity> boardEntityList = boardRepository.findAll();
		return boardEntityList.stream()
			.map(boardConverter::toDto)
			.toList();
	}

	// 게시판 업데이트
	public BoardDto updateBoard(
		BoardRequest boardRequest
	) {
		BoardEntity entity = boardRepository.findById(boardRequest.getId())
			.orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다: " + boardRequest.getId()));

		BoardDto updatedDto = boardConverter.toDto(entity);
		updatedDto.setBoardTitle(boardRequest.getBoardTitle());
		updatedDto.setUpdatedBy("admin"); // 임의로 "admin" 넣음
		updatedDto.setUpdatedAt(LocalDateTime.now());

		boardRepository.save(boardConverter.toEntity(updatedDto));

		return updatedDto;
	}

	public void deleteBoard(Long id) {
		boardRepository.deleteById(id);
	}

	public BoardDto view(Long id) {
		var entity = boardRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("게시판이 존재하지 않습니다.: " + id));
		//

		return boardConverter.toDto(entity);
	}
}