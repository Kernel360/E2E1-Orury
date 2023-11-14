package com.kernel360.orury.domain.comment.service;

import com.kernel360.orury.domain.comment.db.CommentEntity;
import com.kernel360.orury.domain.comment.db.CommentRepository;
import com.kernel360.orury.domain.comment.model.CommentDelRequest;
import com.kernel360.orury.domain.comment.model.CommentDto;
import com.kernel360.orury.domain.comment.model.CommentRequest;
import com.kernel360.orury.domain.notification.service.NotifyService;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.db.PostRepository;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.global.constants.Constant;
import com.kernel360.orury.global.message.errors.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
    private final CommentConverter commentConverter;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotifyService notifyService;

    public CommentDto createComment(CommentRequest commentRequest, String userEmail) {
        PostEntity postEntity = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new RuntimeException(ErrorMessages.THERE_IS_NO_POST.getMessage() + commentRequest.getPostId()));
        var user = userRepository.findByEmailAddr(userEmail)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.THERE_IS_NO_USER.getMessage() + userEmail));
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
                    () -> new RuntimeException(ErrorMessages.THERE_IS_NO_COMMENT.getMessage() + commentEntity.getPId())
            ).getUserId();
        }
        // 댓글이 달렸을 때는 게시글 작성자에게 알림을 보냄
        else
            notifyUserId = postEntity.getUserId();
        sendNotificationToPostAuthor(notifyUserId, saveEntity.getId());

        return commentConverter.toDto(saveEntity);
    }

    private void sendNotificationToPostAuthor(Long userId, Long id) {
        // user Id를 통해 만들어진 emitter 검색
        notifyService.notify(userId, SseEmitter.event().name("comment").data(id));
    }

    public CommentDto updateComment(
            CommentRequest commentRequest
    ){
        Long id = commentRequest.getId();
        Optional<CommentEntity> entity = commentRepository.findById(id);
        CommentEntity updateEntity = entity.orElseThrow(() -> new RuntimeException(ErrorMessages.THERE_IS_NO_COMMENT.getMessage() + id));
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
                () -> new RuntimeException(ErrorMessages.THERE_IS_NO_COMMENT.getMessage() + id)
        );
        var user = userRepository.findByEmailAddr(userEmail).orElseThrow(
                () -> new RuntimeException(ErrorMessages.THERE_IS_NO_USER.getMessage() + userEmail)
        );
        return Objects.equals(user.getId(), comment.getUserId());

    }
}
