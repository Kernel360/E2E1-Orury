package com.kernel360.orury.domain.comment.service;

import com.kernel360.orury.domain.comment.db.CommentEntity;
import com.kernel360.orury.domain.comment.model.CommentDto;
import org.springframework.stereotype.Service;

@Service
public class CommentConverter {
    public CommentDto toDto(CommentEntity commentEntity) {
        return CommentDto.builder()
                .id(commentEntity.getId())
                .postId(commentEntity.getPostId())
                .userId(commentEntity.getUserId())
                .commentContent(commentEntity.getCommentContent())
                .userNickname(commentEntity.getUserNickname())
                .likeCnt(commentEntity.getLikeCnt())
                .pId(commentEntity.getPId())
                .isDelete(commentEntity.isDelete())
                .createdBy(commentEntity.getCreatedBy())
                .createdAt(commentEntity.getCreatedAt())
                .updatedBy(commentEntity.getUpdatedBy())
                .updatedAt(commentEntity.getUpdatedAt())
                .build();
    }

    public CommentEntity toEntity(CommentDto commentdto) {
        return CommentEntity.builder()
                .id(commentdto.getId())
                .postId(commentdto.getPostId())
                .userId(commentdto.getUserId())
                .commentContent(commentdto.getCommentContent())
                .userNickname(commentdto.getUserNickname())
                .likeCnt(commentdto.getLikeCnt())
                .pId(commentdto.getPId())
                .isDelete(commentdto.isDelete())
                .createdBy(commentdto.getCreatedBy())
                .createdAt(commentdto.getCreatedAt())
                .updatedBy(commentdto.getUpdatedBy())
                .updatedAt(commentdto.getUpdatedAt())
                .build();
    }
}
