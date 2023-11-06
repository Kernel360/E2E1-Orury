package com.kernel360.orury.domain.post.controller;

import com.kernel360.orury.domain.board.controller.BoardController;
import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.post.db.PostImageRepository;
import com.kernel360.orury.domain.post.db.PostRepository;
import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.post.model.PostRequest;
import com.kernel360.orury.domain.post.service.PostConverter;
import com.kernel360.orury.domain.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@WebMvcTest(BoardController.class)
class PostControllerTest {

//    private final PostRepository postRepository;
//    private final BoardRepository boardRepository;
//    private final PostConverter postConverter;
//    private final PostImageRepository postImageRepository;

//    @Mock
//    private PostImageRepository postImageRepository;
//
//    @Mock
//    private PostConverter postConverter;
//
//    @Mock
//    private BoardRepository boardRepository;
//
//    @Mock
//    private PostRepository postRepository;

//    @InjectMocks
//    private PostService postService;

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private PostService postService;
//
//    protected MediaType contentType =
//            new MediaType(MediaType.APPLICATION_JSON.getType(),
//                    MediaType.APPLICATION_JSON.getSubtype(),
//                    StandardCharsets.UTF_8);
//
//    @Test
//    void createPostTest() throws Exception {
//        PostRequest postRequest = new PostRequest();
//        postRequest.setUserNickname("test user");
//        postRequest.setPostTitle("test post title");
//        postRequest.setPostContent("test post content");
//
//        PostDto postDto = PostDto.builder()
//                .id(Long.valueOf(15))
//                .boardId(Long.valueOf(4))
//                .postTitle("test post title")
//                .postContent("test post content")
//                .userNickname("test user")
//                .viewCnt(0)
//                .likeCnt(0)
//                .userId(Long.valueOf(8))
//                .createdBy("test admin")
//                .createdAt(LocalDateTime.now())
//                .updatedBy("test admin")
//                .updatedAt(LocalDateTime.now())
//                .thumbnailUrl("asfwghja")
//                .build();
//        given(postService.createPost(postRequest))
//                .willReturn(postDto);
//
//        MvcResult result = mockMvc.perform(post("/api/post")
//                .contentType(contentType))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        System.out.println(result);
//    }

//    @Test
//    void getPostTest() {
//    }
//
//    @Test
//    void getPostListTest() {
//    }
//
//    @Test
//    void updatePostTest() {
//    }
//
//    @Test
//    void deletePostTest() {
//    }
}