package com.kernel360.orury.domain.comment.controller;

import com.kernel360.orury.domain.comment.model.CommentDelRequest;
import com.kernel360.orury.domain.comment.model.CommentDto;
import com.kernel360.orury.domain.comment.model.CommentRequest;
import com.kernel360.orury.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return commentService.createComment(commentRequest);
    }

    // 댓글 수정
    @PatchMapping("")
    public CommentDto updateComment(
            @Valid
            @RequestBody
            CommentRequest commentRequest
    ){
        return commentService.updateComment(commentRequest);
    }

    // 댓글 삭제
    @DeleteMapping("")
    public void deleteComment(
            @Valid
            @RequestBody
            CommentDelRequest commentDelRequest
    ){
        commentService.deleteComment(commentDelRequest);
    }
}
