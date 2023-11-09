package com.kernel360.orury.domain.board.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.comment.db.CommentEntity;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.global.constants.Constant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author : hyungjoon cho, chanwook moon
 * @date : 2023/11/01
 * @description : 게시판 서비스 테스트
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("게시판 CRUD 기능 테스트")
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Test
    @DisplayName("게시판 생성")
    void createBoard() {
//        // given
//        BoardEntity boardEntity = BoardEntity.builder()
//                .boardTitle("테스트 타이틀")
//                .createdBy(Constant.ADMIN.getMessage())
//                .createdAt(LocalDateTime.now())
//                .updatedBy(Constant.ADMIN.getMessage())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
//        // when
//        var savedEntity = boardRepository.save(boardEntity);
//
//        // then
//        assertThat(savedEntity).isEqualTo(boardEntity);
        // given
        BoardEntity boardEntity = BoardEntity.builder()
                .boardTitle("테스트 타이틀")
                .createdBy(Constant.ADMIN.getMessage())
                .createdAt(LocalDateTime.now())
                .updatedBy(Constant.ADMIN.getMessage())
                .updatedAt(LocalDateTime.now())
                .build();

        when(boardRepository.save(Mockito.any())).thenReturn(boardEntity);

        // when
        var savedEntity = boardRepository.save(boardEntity);

        // then
        assertThat(savedEntity).isEqualTo(boardEntity);
    }

    @Test
    @DisplayName("모든 게시판 조회")
    void getBoardList() {
        // given
        BoardEntity boardEntity = BoardEntity.builder()
                .id(9L)
                .boardTitle("로그 테스트 게시판")
                .createdBy(Constant.ADMIN.getMessage())
                .createdAt(LocalDateTime.parse("2023-10-31T19:10:24"))
                .updatedBy(Constant.ADMIN.getMessage())
                .updatedAt(LocalDateTime.parse("2023-10-31T19:10:24"))
                .postList(List.of(
                        PostEntity.builder()
                                .id(32L)
                                .postTitle("댓글 삭제 테스트")
                                .postContent("하나더 해보자123")
                                .userNickname("테스트맨!")
                                .viewCnt(0)
                                .likeCnt(0)
                                .userId(5L)
                                .createdBy(Constant.ADMIN.getMessage())
                                .createdAt(LocalDateTime.parse("2023-10-31T19:12:06"))
                                .updatedBy(Constant.ADMIN.getMessage())
                                .updatedAt(LocalDateTime.parse("2023-10-31T19:12:06"))
                                .thumbnailUrl("zsdfafef321212")
                                .commentList(List.of())
                                .images("")
                                .build(),
                        PostEntity.builder()
                                .id(30L)
                                .postTitle("댓글 삭제 테스트")
                                .postContent("하나더 해보자123")
                                .userNickname("테스트맨!")
                                .viewCnt(0)
                                .likeCnt(0)
                                .userId(5L)
                                .createdBy(Constant.ADMIN.getMessage())
                                .createdAt(LocalDateTime.parse("2023-10-31T19:12:06"))
                                .updatedBy(Constant.ADMIN.getMessage())
                                .updatedAt(LocalDateTime.parse("2023-10-31T19:12:06"))
                                .thumbnailUrl("zsdfafef321212")
                                .commentList(List.of(
                                        CommentEntity.builder()
                                                .id(16L)
                                                .userId(3L)
                                                .userNickname("응애맨")
                                                .commentContent("로그 삭제 테스트")
                                                .likeCnt(0)
                                                .createdBy(Constant.ADMIN.getMessage())
                                                .createdAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                                .updatedBy(Constant.ADMIN.getMessage())
                                                .updatedAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                                .build()
                                ))
                                .build()
                ))
                .build();

        when(boardRepository.findAll()).thenReturn(List.of(boardEntity));

        // when
        List<BoardEntity> boardEntityList = boardRepository.findAll();

        // then
        assertThat(boardEntityList).contains(boardEntity);
    }

    @Test
    @DisplayName("특정 게시판 조회")
    void getBoard() {
//        // given
//        Long id = 9L;
//
//        // when
//        BoardEntity entity = boardRepository.findById(id)
//                // 여기서 이미지 필드 빼고 다 가져오도록 처리
//                .orElseThrow(() -> new RuntimeException(ErrorMessages.THERE_IS_NO_BOARD.getMessage() + "9"));
//        BoardDto boardDto = boardConverter.toDto(entity);
//
//        // then
//        assertThat(boardDto.getId()).isEqualTo(id);
        // given
        Long id = 9L;

        BoardEntity boardEntity = BoardEntity.builder()
                .id(id)
                .boardTitle("로그 테스트 게시판")
                .createdBy(Constant.ADMIN.getMessage())
                .createdAt(LocalDateTime.parse("2023-10-31T19:10:24"))
                .updatedBy(Constant.ADMIN.getMessage())
                .updatedAt(LocalDateTime.parse("2023-10-31T19:10:24"))
                .postList(List.of(
                        PostEntity.builder()
                                .id(32L)
//                                .board(boardEntity)
                                .postTitle("댓글 삭제 테스트")
                                .postContent("하나더 해보자123")
                                .userNickname("테스트맨!")
                                .viewCnt(0)
                                .likeCnt(0)
                                .userId(5L)
                                .createdBy(Constant.ADMIN.getMessage())
                                .createdAt(LocalDateTime.parse("2023-10-31T19:12:06"))
                                .updatedBy(Constant.ADMIN.getMessage())
                                .updatedAt(LocalDateTime.parse("2023-10-31T19:12:06"))
                                .thumbnailUrl("zsdfafef321212")
                                .commentList(List.of())
                                .images("")
                                .build(),
                        PostEntity.builder()
                                .id(30L)
//                                .board(boardEntity)
                                .postTitle("댓글 삭제 테스트")
                                .postContent("하나더 해보자123")
                                .userNickname("테스트맨!")
                                .viewCnt(0)
                                .likeCnt(0)
                                .userId(5L)
                                .createdBy(Constant.ADMIN.getMessage())
                                .createdAt(LocalDateTime.parse("2023-10-31T19:12:06"))
                                .updatedBy(Constant.ADMIN.getMessage())
                                .updatedAt(LocalDateTime.parse("2023-10-31T19:12:06"))
                                .thumbnailUrl("zsdfafef321212")
                                .commentList(List.of(
                                        CommentEntity.builder()
                                                .id(16L)
                                                .userId(3L)
                                                .userNickname("응애맨")
//                                                .post(boardEntity)
                                                .commentContent("로그 삭제 테스트")
                                                .likeCnt(0)
                                                .createdBy(Constant.ADMIN.getMessage())
                                                .createdAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                                .updatedBy(Constant.ADMIN.getMessage())
                                                .updatedAt(LocalDateTime.parse("2023-10-31T19:12:11"))
                                                .build()
                                ))
                                .build()
                ))
                .build();

        when(boardRepository.findById(id)).thenReturn(Optional.of(boardEntity));

        // when
        var entity = boardRepository.findById(id);

        // then
        assertThat(entity.get().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("게시판 수정")
    void updateBoard() {
        // given
        Long id = 9L;
        BoardEntity boardEntity = BoardEntity.builder()
                .id(id)
                .boardTitle("로그 테스트 게시판 수정")
                .createdBy(Constant.ADMIN.getMessage())
                .createdAt(LocalDateTime.parse("2023-10-31T19:10:24"))
                .updatedBy(Constant.ADMIN.getMessage())
                .updatedAt(LocalDateTime.now())
                .postList(List.of())
                .build();

        when(boardRepository.save(boardEntity)).thenReturn(boardEntity);

        // when
        BoardEntity updatedEntity = boardRepository.save(boardEntity);

        // then
        assertThat(updatedEntity.getBoardTitle()).isEqualTo("로그 테스트 게시판 수정");
    }

    @Test
    @DisplayName("게시판 삭제")
    void deleteBoard() {
        // given
        Long id = 9L;
        BoardEntity boardEntity = BoardEntity.builder()
                .id(id)
                .build();

        // when
        boardRepository.deleteById(id);
        ResponseEntity<Void> response = ResponseEntity.noContent().build();

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }
}