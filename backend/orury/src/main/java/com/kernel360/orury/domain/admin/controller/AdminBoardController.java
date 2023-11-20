package com.kernel360.orury.domain.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kernel360.orury.domain.board.model.BoardDto;
import com.kernel360.orury.domain.board.model.BoardRequest;
import com.kernel360.orury.domain.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminBoardController {

	private final BoardService boardService;

	@GetMapping
	public String index() {
		return "index";
	}

	@GetMapping("/board")
	public String board(Model model) {
		List<BoardDto> boardList = boardService.getBoardList();
		model.addAttribute("boardList", boardList);
		return "board-table";
	}

	@PostMapping("/board")
	public String createBoard(
		@Valid @ModelAttribute BoardRequest boardRequest
	) {
		boardService.createBoard(boardRequest);
		return "redirect:/admin/board";
	}

	@PostMapping("/updateBoard")
	public String updateBoard(
		@Valid @ModelAttribute BoardRequest boardRequest
	) {
		boardService.updateBoard(boardRequest);
		return "redirect:/admin/board";
	}

	@PostMapping("/deleteBoard")
	public String deleteBoard(
		@RequestParam("boardId") Long boardId
	) {
		boardService.deleteBoard(boardId);
		return "redirect:/admin/board";
	}

}
