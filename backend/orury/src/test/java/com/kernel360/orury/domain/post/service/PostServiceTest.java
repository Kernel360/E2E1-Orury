package com.kernel360.orury.domain.post.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.comment.db.CommentEntity;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.db.PostRepository;
import com.kernel360.orury.global.constants.Constant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 CRUD 기능 테스트")
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("게시글 생성")
    void createPost() {
        Long id = 10L;
        PostEntity postEntity = PostEntity.builder()
                .id(id)
                .postTitle("테스트 타이틀")
                .postContent("테스트 본문")
                .userId(1234L)
                .createdBy(Constant.ADMIN.getMessage())
                .createdAt(LocalDateTime.now())
                .updatedBy(Constant.ADMIN.getMessage())
                .updatedAt(LocalDateTime.now())
                .commentList(List.of(
                        CommentEntity.builder()
                                .id(16L)
                                .userId(3L)
                                .commentContent("로그 삭제 테스트")
                                .likeCnt(0)
                                .createdBy(Constant.ADMIN.getMessage())
                                .createdAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                .updatedBy(Constant.ADMIN.getMessage())
                                .updatedAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                .build()
                ))
                .build();

        when(postRepository.save(postEntity)).thenReturn(postEntity);

        // when
        var savedEntity = postRepository.save(postEntity);

        // then
        assertThat(savedEntity).isEqualTo(postEntity);

    }

    @Test
    @DisplayName("특정 게시글 조회")
    void getPost() {
        // given
        Long id = 9L;
        PostEntity postEntity = PostEntity.builder()
                .id(id)
                .postTitle("테스트 타이틀")
                .postContent("테스트 본문")
                .userId(1234L)
                .createdBy(Constant.ADMIN.getMessage())
                .createdAt(LocalDateTime.now())
                .updatedBy(Constant.ADMIN.getMessage())
                .updatedAt(LocalDateTime.now())
                .images("3lPyhi2.jpg,RD1Qemr.jpg")
                .commentList(List.of(
                        CommentEntity.builder()
                                .id(16L)
                                .userId(3L)
                                .commentContent("로그 삭제 테스트")
                                .likeCnt(0)
                                .createdBy(Constant.ADMIN.getMessage())
                                .createdAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                .updatedBy(Constant.ADMIN.getMessage())
                                .updatedAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                .build()
                ))
                .build();

        // when
        when(postRepository.findById(id)).thenReturn(Optional.of(postEntity));
        Optional<PostEntity> findPostEntity = postRepository.findById(id);

        // than
        assertThat(findPostEntity.get().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("게시글 수정")
    void updatePost() {
        // given
        Long id = 10L;
        PostEntity postEntity = PostEntity.builder()
                .id(id)
                .postTitle("테스트 타이틀")
                .postContent("테스트 본문")
                .userId(1234L)
                .createdBy(Constant.ADMIN.getMessage())
                .createdAt(LocalDateTime.now())
                .updatedBy(Constant.ADMIN.getMessage())
                .updatedAt(LocalDateTime.now())
                .images("3lPyhi2.jpg,RD1Qemr.jpg")
                .commentList(List.of(
                        CommentEntity.builder()
                                .id(16L)
                                .userId(3L)
                                .commentContent("로그 삭제 테스트")
                                .likeCnt(0)
                                .createdBy(Constant.ADMIN.getMessage())
                                .createdAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                .updatedBy(Constant.ADMIN.getMessage())
                                .updatedAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                .build()
                ))
                .build();

        // when
        when(postRepository.save(postEntity)).thenReturn(postEntity);
        PostEntity updatedEntity = postRepository.save(postEntity);

        // then
        assertThat(updatedEntity.getPostTitle()).isEqualTo("테스트 타이틀");
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() {
        // given
        Long id = 9L;
        PostEntity postEntity = PostEntity.builder()
                .id(id)
                .build();

        // when
        postRepository.deleteById(id);
        ResponseEntity<Void> response = ResponseEntity.noContent().build();

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(204);

    }

    @Test
    @DisplayName("모든 게시글 조회")
    void getPostList() {
        // given
        List<PostEntity> postEntityList = List.of(
                PostEntity.builder()
                        .id(1L)
                        .postTitle("테스트 타이틀")
                        .postContent("테스트 본문")
                        .userId(1234L)
                        .images("3lPyhi2.jpg,RD1Qemr.jpg")
                        .createdBy(Constant.ADMIN.getMessage())
                        .createdAt(LocalDateTime.now())
                        .updatedBy(Constant.ADMIN.getMessage())
                        .updatedAt(LocalDateTime.now())
                        .commentList(List.of(
                                CommentEntity.builder()
                                        .id(16L)
                                        .userId(3L)
                                        .commentContent("로그 삭제 테스트")
                                        .likeCnt(0)
                                        .createdBy(Constant.ADMIN.getMessage())
                                        .createdAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                        .updatedBy(Constant.ADMIN.getMessage())
                                        .updatedAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                        .build()
                        ))
                        .build(),
                PostEntity.builder()
                        .id(5L)
                        .postTitle("테스트 타2323이틀")
                        .postContent("테스트 본문")
                        .userId(1234L)
                        .images("3lPyhi2.jpg,RD1Qemr.jpg")
                        .createdBy(Constant.ADMIN.getMessage())
                        .createdAt(LocalDateTime.now())
                        .updatedBy(Constant.ADMIN.getMessage())
                        .updatedAt(LocalDateTime.now())
                        .commentList(List.of(
                                CommentEntity.builder()
                                        .id(16L)
                                        .userId(3L)
                                        .commentContent("로그 삭제 테스트")
                                        .likeCnt(0)
                                        .createdBy(Constant.ADMIN.getMessage())
                                        .createdAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                        .updatedBy(Constant.ADMIN.getMessage())
                                        .updatedAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                        .build()
                        ))
                        .build(),
                PostEntity.builder()
                        .id(17L)
                        .postTitle("테스트 타이틀123")
                        .postContent("테스트 본문")
                        .userId(1234L)
                        .images("3lPyhi2.jpg,RD1Qemr.jpg")
                        .createdBy(Constant.ADMIN.getMessage())
                        .createdAt(LocalDateTime.now())
                        .updatedBy(Constant.ADMIN.getMessage())
                        .updatedAt(LocalDateTime.now())
                        .commentList(List.of(
                                CommentEntity.builder()
                                        .id(16L)
                                        .userId(3L)
                                        .commentContent("로그 삭제 테스트")
                                        .likeCnt(0)
                                        .createdBy(Constant.ADMIN.getMessage())
                                        .createdAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                        .updatedBy(Constant.ADMIN.getMessage())
                                        .updatedAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                        .build()
                        ))
                        .build()
        );
        when(postRepository.findAll()).thenReturn(postEntityList);

        // when
        List<PostEntity> retrievedPostList = postRepository.findAll();

        // then
        // 해당 코드는 순서를 검증하지 않음, 순서를 검증하는 방법을 알아볼 것.
        assertThat(retrievedPostList).containsExactlyInAnyOrderElementsOf(postEntityList);

    }
}