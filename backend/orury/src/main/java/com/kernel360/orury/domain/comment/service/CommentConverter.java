package com.kernel360.orury.domain.comment.service;

import com.kernel360.orury.domain.comment.db.CommentEntity;
import com.kernel360.orury.domain.comment.model.CommentDto;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.db.PostRepository;
import com.kernel360.orury.global.message.errors.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentConverter {

    private final PostRepository postRepository;
    public CommentDto toDto(CommentEntity commentEntity) {
        return CommentDto.builder()
                .id(commentEntity.getId())
                .postId(commentEntity.getPost().getId())
                .userId(commentEntity.getUserId())
                .commentContent(commentEntity.getCommentContent())
                .userNickname(commentEntity.getUserNickname())
                .likeCnt(commentEntity.getLikeCnt())
                .pId(commentEntity.getPId())
                .createdBy(commentEntity.getCreatedBy())
                .createdAt(commentEntity.getCreatedAt())
                .updatedBy(commentEntity.getUpdatedBy())
                .updatedAt(commentEntity.getUpdatedAt())
                .build();
    }

    public CommentEntity toEntity(CommentDto commentdto) {
        PostEntity postEntity = postRepository.findById(commentdto.getPostId())
                .orElseThrow(
                        () -> new RuntimeException(ErrorMessages.THERE_IS_NO_POST.getMessage() + commentdto.getPostId())
                );
        return CommentEntity.builder()
                .id(commentdto.getId())
                .post(postEntity)
                .userId(commentdto.getUserId())
                .commentContent(commentdto.getCommentContent())
                .userNickname(commentdto.getUserNickname())
                .likeCnt(commentdto.getLikeCnt())
                .pId(commentdto.getPId())
                .createdBy(commentdto.getCreatedBy())
                .createdAt(commentdto.getCreatedAt())
                .updatedBy(commentdto.getUpdatedBy())
                .updatedAt(commentdto.getUpdatedAt())
                .build();
    }
}
