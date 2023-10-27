package com.kernel360.orury.domain.comment.service;

import com.kernel360.orury.domain.comment.db.CommentEntity;
import com.kernel360.orury.domain.comment.db.CommentRepository;
import com.kernel360.orury.domain.comment.model.CommentDelRequest;
import com.kernel360.orury.domain.comment.model.CommentDto;
import com.kernel360.orury.domain.comment.model.CommentRequest;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.db.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * author : hyungjoon cho
 * date : 2023/10/26
 * description : 댓글 기능구현
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final PostRepository postRepository;

    public CommentDto createComment(CommentRequest commentRequest) {
        PostEntity postEntity = postRepository.findById(commentRequest.getPostId()).get();

        CommentEntity commentEntity = CommentEntity.builder()
                .userId(commentRequest.getUserId())
                .post(postEntity)
                .commentContent(commentRequest.getCommentContent())
                .userNickname(commentRequest.getUserNickname())
                // 대댓글과 본댓글 판별
                .pId(commentRequest.getId() == null ? null : commentRequest.getId())
                .createdBy("admin")
                .createdAt(LocalDateTime.now())
                .updatedBy("admin")
                .updatedAt(LocalDateTime.now())
                .build();

        CommentEntity saveEntity = commentRepository.save(commentEntity);

        return commentConverter.toDto(saveEntity);
    }

    public CommentDto updateComment(
            CommentRequest commentRequest
    ){
        Long id = commentRequest.getId();
        Optional<CommentEntity> entity = commentRepository.findById(id);
        CommentEntity updateEntity = entity.orElseThrow(() -> new RuntimeException("해당 댓글이 없습니다." + id));
        CommentDto updateDto = commentConverter.toDto(updateEntity);
        updateDto.setCommentContent(commentRequest.getCommentContent());
        updateDto.setUpdatedBy("admin");
        updateDto.setUpdatedAt(LocalDateTime.now());
        CommentEntity saveEntity = commentConverter.toEntity(updateDto);
        commentRepository.save(saveEntity);

        return updateDto;
    }

    public void deleteComment(
            CommentDelRequest commentDelRequest
    ){
        commentRepository.deleteById(commentDelRequest.getId());
        log.info("댓글이 삭제되었습니다. : {}",commentDelRequest.getId());
    }

    public List<CommentDto> findAllByPostId(Long postId) {
        List<CommentEntity> commentEntityList = commentRepository.findAllByPostIdAndIsDeleteOrderByIdDesc(postId, false);
        return commentEntityList.stream()
                .map(commentConverter::toDto)
                .toList();
    }

}
