package com.kernel360.orury.domain.board.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.board.model.BoardDto;
import com.kernel360.orury.domain.board.model.BoardRequest;
import com.kernel360.orury.domain.post.db.PostRepository;
import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.post.service.PostConverter;
import com.kernel360.orury.global.constants.Constant;
import com.kernel360.orury.global.error.code.BoardErrorCode;
import com.kernel360.orury.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

	public final BoardRepository boardRepository;
	public final BoardConverter boardConverter;
	public final PostRepository postRepository;
	public final PostConverter postConverter;

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

		return boardConverter.toDto(saveEntity, false);
	}

	// 게시판 조회
	public List<BoardDto> getBoardList() {
		List<BoardEntity> boardEntityList = boardRepository.findAll();
		return boardEntityList.stream()
			.map((BoardEntity boardEntity) -> boardConverter.toDto(boardEntity, false))
			.toList();
	}

	// 게시판 업데이트
	public BoardDto updateBoard(
		BoardRequest boardRequest
	) {
		BoardEntity entity = boardRepository.findById(boardRequest.getId())
			.orElseThrow(() -> new BusinessException(BoardErrorCode.THERE_IS_NO_BOARD));

		BoardDto updatedDto = boardConverter.toDto(entity, false);
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
			.orElseThrow(() -> new BusinessException(BoardErrorCode.THERE_IS_NO_BOARD));

		return boardConverter.toDto(entity, true);
	}

	public List<PostDto> getNoticeBoard(Long id) {
		var noticeList = postRepository.findAllByBoardIdOrderByIdDesc(id);
		if(noticeList.isEmpty()) {
			throw new BusinessException(BoardErrorCode.THERE_IS_NO_BOARD);
		}

		return noticeList.stream()
			.map(postConverter::toNoticeDto) // 각 엔티티를 Dto로 변환
			.toList();
	}
}