package com.kernel360.orury.domain.comment.service;

import com.kernel360.orury.domain.comment.db.*;
import com.kernel360.orury.domain.comment.model.CommentDto;
import com.kernel360.orury.domain.comment.model.CommentLikeDto;
import com.kernel360.orury.domain.comment.model.CommentLikeRequest;
import com.kernel360.orury.domain.comment.model.CommentRequest;
import com.kernel360.orury.domain.notification.service.NotifyService;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.db.PostRepository;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.global.constants.Constant;
import com.kernel360.orury.global.error.code.CommentErrorCode;
import com.kernel360.orury.global.error.code.PostErrorCode;
import com.kernel360.orury.global.error.code.UserErrorCode;
import com.kernel360.orury.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * author : hyungjoon cho
 * date : 2023/10/26
 * description : 댓글 기능구현
 */
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentConverter commentConverter;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotifyService notifyService;

    public CommentDto createComment(CommentRequest commentRequest, String userEmail) {
        PostEntity postEntity = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new BusinessException(PostErrorCode.THERE_IS_NO_POST));
        var user = userRepository.findByEmailAddr(userEmail)
                .orElseThrow(() -> new BusinessException(UserErrorCode.THERE_IS_NO_USER));
        Long userId = user.getId();

        CommentEntity commentEntity = CommentEntity.builder()
                .userId(userId)
                .post(postEntity)
                .commentContent(commentRequest.getCommentContent())
                // 대댓글과 본댓글 판별
                .pId(commentRequest.getId() == null ? null : commentRequest.getId())
                .createdBy(Constant.ADMIN.getMessage())
                .createdAt(LocalDateTime.now())
                .updatedBy(Constant.ADMIN.getMessage())
                .updatedAt(LocalDateTime.now())
                .build();

        CommentEntity saveEntity = commentRepository.save(commentEntity);

        // 대댓글이 달렸을 때 댓글 작성자에게 알림을 보냄
        Long notifyUserId = null;
        if( commentEntity.getPId() != null ) {
            notifyUserId = commentRepository.findById(commentEntity.getPId()).orElseThrow(
                    () -> new BusinessException(CommentErrorCode.THERE_IS_NO_COMMENT)
            ).getUserId();
            if (!Objects.equals(notifyUserId, saveEntity.getUserId())) {
                sendNotificationToPostAuthor(notifyUserId, postEntity.getPostTitle() + "의 댓글 답변 달림");
            }
        }
        // 댓글이 달렸을 때는 게시글 작성자에게 알림을 보냄
        else {
            notifyUserId = postEntity.getUserId();
            if (!Objects.equals(notifyUserId, saveEntity.getUserId())) {
                sendNotificationToPostAuthor(notifyUserId, postEntity.getPostTitle() + " 댓글 달림");
            }
        }

        return commentConverter.toDto(saveEntity);
    }

    private void sendNotificationToPostAuthor(Long userId, String message) {
        // user Id를 통해 만들어진 emitter 검색
        notifyService.notify(userId, message , "comment");
    }

    public CommentDto updateComment(
            CommentRequest commentRequest
    ){
        Long id = commentRequest.getId();
        Optional<CommentEntity> entity = commentRepository.findById(id);
        CommentEntity updateEntity = entity.orElseThrow(() -> new BusinessException(CommentErrorCode.THERE_IS_NO_COMMENT));
        CommentDto updateDto = commentConverter.toDto(updateEntity);
        updateDto.setCommentContent(commentRequest.getCommentContent());
        updateDto.setUpdatedBy(Constant.ADMIN.getMessage());
        updateDto.setUpdatedAt(LocalDateTime.now());
        CommentEntity saveEntity = commentConverter.toEntity(updateDto);
        commentRepository.save(saveEntity);

        return updateDto;
    }

    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

    public List<CommentDto> findAllByPostId(Long postId) {
        List<CommentEntity> commentEntityList = commentRepository.findAllByPostIdOrderByIdDesc(postId);
        return commentEntityList.stream()
                .map(commentConverter::toDto)
                .toList();
    }

    public boolean isWriter(String userEmail, Long id) {
        var comment = commentRepository.findById(id).orElseThrow(
                () -> new BusinessException(CommentErrorCode.THERE_IS_NO_COMMENT)
        );
        var user = userRepository.findByEmailAddr(userEmail).orElseThrow(
                () -> new BusinessException(UserErrorCode.THERE_IS_NO_USER)
        );
        return Objects.equals(user.getId(), comment.getUserId());
    }

    // 댓글 좋아요 상태 업데이트
    public CommentLikeDto updateCommentLike(CommentLikeRequest commentLikeRequest) {
        boolean isLike = commentLikeRequest.isLike();

        CommentLikePK commentLikePK = new CommentLikePK();
        commentLikePK.setCommentId(commentLikeRequest.getCommentId());
        commentLikePK.setUserId(commentLikeRequest.getUserId());

        var entity = CommentLikeEntity.builder()
                .commentLikePK(commentLikePK)
                .createdBy(commentLikeRequest.getUserId().toString())
                .createdAt(LocalDateTime.now())
                .updatedBy(commentLikeRequest.getUserId().toString())
                .updatedAt(LocalDateTime.now())
                .build();

        CommentLikeDto likeDto = commentConverter.toLikeDto(entity);

        if (isLike) {
            commentLikeRepository.save(entity);
        } else {
            commentLikeRepository.delete(entity);
        }
        return likeDto;
    }
}
