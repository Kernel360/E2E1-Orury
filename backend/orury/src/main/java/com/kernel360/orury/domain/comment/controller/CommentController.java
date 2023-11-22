package com.kernel360.orury.domain.comment.controller;

import com.kernel360.orury.domain.comment.model.CommentDto;
import com.kernel360.orury.domain.comment.model.CommentLikeDto;
import com.kernel360.orury.domain.comment.model.CommentLikeRequest;
import com.kernel360.orury.domain.comment.model.CommentRequest;
import com.kernel360.orury.domain.comment.service.CommentService;
import com.kernel360.orury.global.constants.Constant;
import com.kernel360.orury.global.error.code.AuthorizationErrorCode;
import com.kernel360.orury.global.message.info.InfoMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("")
    public CommentDto createComment(
            @Valid
            @RequestBody
            CommentRequest commentRequest
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var userEmail = authentication.getName();

        return commentService.createComment(commentRequest, userEmail);
    }

    // 댓글 수정
    @PatchMapping("")
    public ResponseEntity<String> updateComment(
            @Valid
            @RequestBody
            CommentRequest commentRequest
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        if(commentService.isWriter(userEmail, commentRequest.getId())) {
            commentService.updateComment(commentRequest);
            return ResponseEntity.ok(InfoMessages.COMMENT_UPDATED.getMessage());
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(AuthorizationErrorCode.THERE_IS_NO_AUTHORITY.getMessage());
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long commentId
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals(Constant.ROLE_ADMIN.getMessage()));

        if(commentService.isWriter(userEmail, commentId) || isAdmin) {
            commentService.deleteComment(commentId);
            return ResponseEntity.ok(InfoMessages.COMMENT_DELETED.getMessage());
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    "댓글 삭제" + AuthorizationErrorCode.THERE_IS_NO_AUTHORITY.getMessage());
        }
    }

    // 댓글 좋아요 업데이트
    @PatchMapping("/like")
    public CommentLikeDto updateCommentLike(
            @Valid
            @RequestBody CommentLikeRequest commentLikeRequest
    ) {
        return commentService.updateCommentLike(commentLikeRequest);
    }
}
