package com.kernel360.orury.domain.board.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.board.model.BoardDto;
import com.kernel360.orury.domain.board.model.BoardRequest;
import com.kernel360.orury.global.constants.Constant;
import com.kernel360.orury.global.message.errors.ErrorMessages;
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
			.createdBy(Constant.ADMIN.getMessage())
			.createdAt(LocalDateTime.now())
			.updatedBy(Constant.ADMIN.getMessage())
			.updatedAt(LocalDateTime.now())
			.build();

		var saveEntity = boardRepository.save(entity);

		return boardConverter.toDto(saveEntity);
	}

	// 게시판 조회
	public List<BoardDto> getBoardList() {
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
			.orElseThrow(() -> new RuntimeException(ErrorMessages.THERE_IS_NO_BOARD.getMessage() + boardRequest.getId()));

		BoardDto updatedDto = boardConverter.toDto(entity);
		updatedDto.setBoardTitle(boardRequest.getBoardTitle());
		updatedDto.setUpdatedBy(Constant.ADMIN.getMessage());
		updatedDto.setUpdatedAt(LocalDateTime.now());

		boardRepository.save(boardConverter.toEntity(updatedDto));

		return updatedDto;
	}

	public void deleteBoard(Long id) {
		boardRepository.deleteById(id);
	}

	public BoardDto getBoard(Long id) {
		var entity = boardRepository.findById(id)
			.orElseThrow(() -> new RuntimeException(ErrorMessages.THERE_IS_NO_BOARD.getMessage() + id));

		return boardConverter.toDto(entity);
	}
}