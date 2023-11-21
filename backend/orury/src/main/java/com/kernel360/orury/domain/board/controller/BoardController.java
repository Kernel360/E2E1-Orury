package com.kernel360.orury.domain.board.controller;

import com.kernel360.orury.domain.board.model.BoardDto;
import com.kernel360.orury.domain.board.model.BoardRequest;
import com.kernel360.orury.domain.board.service.BoardService;

import com.kernel360.orury.domain.post.model.PostDto;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardService;

	// 게시판 생성
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("")
	public BoardDto createBoard(
		@Valid
		@RequestBody
		BoardRequest boardRequest
	) {
		return boardService.createBoard(boardRequest);
	}

	// 모든 게시판 조회
	@GetMapping("")
	public List<BoardDto> getBoardList() {
		return boardService.getBoardList();
	}

	// 게시판 업데이트
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("")
	public BoardDto updateBoard(
		@Valid
		@RequestBody
		BoardRequest boardRequest
	) {
		return boardService.updateBoard(boardRequest);
	}

	// 특정 게시판 조회
	@GetMapping("/{id}")
	public ResponseEntity<BoardDto> getBoard(
		@PathVariable Long id
	) {
		return ResponseEntity.ok(boardService.getBoard(id));
	}

	// 공지 간단 조회
	@GetMapping("/notice")
	public ResponseEntity<List<PostDto>> getNoticeBoard(
	) {
		return ResponseEntity.ok(boardService.getNoticeBoard(1L));
	}

	// 게시판 삭제
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public void deleteBoard(
		@PathVariable
		Long id
	) {
		boardService.deleteBoard(id);
	}
}
