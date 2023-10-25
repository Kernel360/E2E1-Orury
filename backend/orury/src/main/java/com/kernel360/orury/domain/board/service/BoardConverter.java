package com.kernel360.orury.domain.board.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.model.BoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardConverter {

    public BoardDto toDto(BoardEntity boardEntity){
        return BoardDto.builder()
                .id(boardEntity.getId())
                .boardTitle(boardEntity.getBoardTitle())
                .createdBy(boardEntity.getCreatedBy())
                .createdAt(boardEntity.getCreatedAt())
                .updatedBy(boardEntity.getUpdatedBy())
                .updatedAt(boardEntity.getUpdatedAt())
                .build();
    }
}
