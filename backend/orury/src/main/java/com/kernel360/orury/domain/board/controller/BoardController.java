package com.kernel360.orury.domain.board.controller;

import com.kernel360.orury.domain.board.model.BoardDto;
import com.kernel360.orury.domain.board.model.BoardRequest;
import com.kernel360.orury.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;


    @PostMapping("")
    public BoardDto createBoard(
            @Valid
            @RequestBody
            BoardRequest boardRequest
    ){
        return boardService.createBoard(boardRequest);
    }

    @GetMapping("")
    public List<BoardDto> getBoard() {
        return boardService.getBoard();
    }

    @PatchMapping("")
    public BoardDto updateBoard(
            @Valid
            @RequestBody
            BoardRequest boardRequest
    ){
        return boardService.updateBoard(boardRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteBoard(
        @PathVariable
        Long id
    ) {
        boardService.deleteBoard(id);
    }
}
