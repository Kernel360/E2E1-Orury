package com.kernel360.orury.domain.board.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.board.model.BoardDto;
import com.kernel360.orury.domain.board.model.BoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardService {

    public final BoardRepository boardRepository;

    public final BoardConverter boardConverter;

    public BoardDto create(
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
}
