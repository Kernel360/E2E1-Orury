package com.kernel360.orury.domain.post.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.comment.service.CommentConverter;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.global.message.errors.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostConverter {

    private final BoardRepository boardRepository;
    private final CommentConverter commentConverter;

    public PostDto toDto(PostEntity postEntity) {

        var commentList = postEntity.getCommentList()
                .stream()
                .map(commentConverter::toDto)
                .toList();

        return PostDto.builder()
                .id(postEntity.getId())
                .boardId(postEntity.getBoard().getId())
                .postTitle(postEntity.getPostTitle())
                .postContent(postEntity.getPostContent())
                .userNickname(postEntity.getUserNickname())
                .viewCnt(postEntity.getViewCnt())
                .likeCnt(postEntity.getLikeCnt())
                .userId(postEntity.getUserId())
                .thumbnailUrl(postEntity.getThumbnailUrl())
                .imageList(postEntity.getImages() == null ? List.of() : Arrays.stream(postEntity.getImages().split(",")).toList())
                .commentList(commentList)
                .createdBy(postEntity.getCreatedBy())
                .createdAt(postEntity.getCreatedAt())
                .updatedBy(postEntity.getUpdatedBy())
                .updatedAt(postEntity.getUpdatedAt())
                .build();
    }

    public PostEntity toEntity(PostDto postDto) {
        BoardEntity boardEntity = boardRepository.findById(postDto.getBoardId())
                .orElseThrow(
                        () -> new RuntimeException(ErrorMessages.THERE_IS_NO_BOARD.getMessage() + postDto.getBoardId())
                );

        return PostEntity.builder()
                .id(postDto.getId())
                .board(boardEntity)
                .postTitle(postDto.getPostTitle())
                .postContent(postDto.getPostContent())
                .userNickname(postDto.getUserNickname())
                .viewCnt(postDto.getViewCnt())
                .likeCnt(postDto.getLikeCnt())
                .userId(postDto.getUserId())
                .thumbnailUrl(postDto.getThumbnailUrl())
                .createdBy(postDto.getCreatedBy())
                .createdAt(postDto.getCreatedAt())
                .updatedBy(postDto.getUpdatedBy())
                .updatedAt(postDto.getUpdatedAt())
                .build();
    }
}
