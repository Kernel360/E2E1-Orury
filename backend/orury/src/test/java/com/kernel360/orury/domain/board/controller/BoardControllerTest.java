package com.kernel360.orury.domain.board.controller;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.model.BoardDto;
import com.kernel360.orury.domain.board.service.BoardService;
import com.kernel360.orury.global.constants.Constant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * author : hyungjoon cho, chanwook moon
 * date : 2023/11/01
 * description : 게시판 컨트롤러 테스트
 */

@WebMvcTest(BoardController.class)
class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);

    // 게시판 생성
    @Test
    public void createBoard(){
        // given
//        given()

        // when

        // then
    }

    // 모든 게시판 조회
    @Test
    void getBoardList() throws Exception {
        // 강의에서 나온 코드
//        BoardDto boardDto = BoardDto.builder()
//                .boardTitle("테스트게시판44")
//                .id(3L)
//                .createdBy(Constant.ADMIN.getMessage())
//                .createdAt(LocalDateTime.now())
//                .updatedBy(Constant.ADMIN.getMessage())
//                .updatedAt(LocalDateTime.now())
//                .build();
//        given(boardService.getBoardList())
//                .willReturn(List.of());
//
//        mockMvc.perform(get("/api/board/all").contentType(contentType))
//                .andExpect(status().isOk())
//                .andExpect(
//                        // 응답값의 0번째 인덱스의 board_title이 테스트게시판44 일것이라고 예측 (db마다 다름)
//                        jsonPath("$.[0].boardTitle",
//                        is("테스트게시판44"))
//                );



    }

    // 게시판 업데이트
    @Test
    void updateBoard() {
    }

    // 특정 게시판 조회
    @Test
    void getBoard() {
    }

    // 게시판 삭제
    @Test
    void deleteBoard() {
    }
}