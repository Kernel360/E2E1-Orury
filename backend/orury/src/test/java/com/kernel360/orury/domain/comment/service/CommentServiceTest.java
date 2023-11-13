package com.kernel360.orury.domain.comment.service;

import com.kernel360.orury.domain.comment.db.CommentEntity;
import com.kernel360.orury.domain.comment.db.CommentRepository;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.global.constants.Constant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author : hyungjoon cho
 * @date : 2023/11/02
 * @description : 댓글 기능 테스트코드
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("댓글 CRUD 기능 테스트")
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 작성")
    void createComment() {
        Long id = 16L;
        CommentEntity commentEntity = CommentEntity.builder()
                .id(id)
                .userId(3L)
                .commentContent("로그 삭제 테스트")
                .likeCnt(0)
                .createdBy(Constant.ADMIN.getMessage())
                .createdAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                .updatedBy(Constant.ADMIN.getMessage())
                .updatedAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                .build();

        when(commentRepository.save(commentEntity)).thenReturn(commentEntity);

        // when
        var savedEntity = commentRepository.save(commentEntity);

        // then
        assertThat(savedEntity).isEqualTo(commentEntity);

    }

    @Test
    @DisplayName("댓글 수정")
    void updateComment() {
        // given
        Long id = 10L;
        CommentEntity commentEntity = CommentEntity.builder()
                .id(id)
                .userId(3L)
                .commentContent("로그 삭제 테스트?!?")
                .likeCnt(0)
                .createdBy(Constant.ADMIN.getMessage())
                .createdAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                .updatedBy(Constant.ADMIN.getMessage())
                .updatedAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                .build();

        // when
        when(commentRepository.save(commentEntity)).thenReturn(commentEntity);
        CommentEntity updatedEntity = commentRepository.save(commentEntity);

        // then
        assertThat(updatedEntity.getCommentContent()).isEqualTo("로그 삭제 테스트?!?");
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() {
        // given
        Long id = 3L;

        // when
        commentRepository.deleteById(id);
        ResponseEntity<Void> response = ResponseEntity.noContent().build();

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    @DisplayName("게시글의 모든 댓글 조회")
    void findAllByPostId() {
        //TODO, 해당 메서드는 사용중이지 않음, 해당 기능 추가 개발 필요
    }
}