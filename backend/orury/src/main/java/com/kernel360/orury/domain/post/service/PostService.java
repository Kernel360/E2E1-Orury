package com.kernel360.orury.domain.post.service;

import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.post.db.*;
import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.post.model.PostLikeDto;
import com.kernel360.orury.domain.post.model.PostLikeRequest;
import com.kernel360.orury.domain.post.model.PostRequest;
import com.kernel360.orury.domain.user.db.UserEntity;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.global.common.Api;
import com.kernel360.orury.global.common.Pagination;
import com.kernel360.orury.global.constants.Constant;
import com.kernel360.orury.global.error.code.BoardErrorCode;
import com.kernel360.orury.global.error.code.PostErrorCode;
import com.kernel360.orury.global.error.code.UserErrorCode;
import com.kernel360.orury.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final BoardRepository boardRepository;
    private final PostConverter postConverter;
    private final UserRepository userRepository;

    public PostDto createPost(
            PostRequest postRequest,
            String userEmail
    ) {
        var user = userRepository.findByEmailAddr(userEmail)
                .orElseThrow(() -> new BusinessException(UserErrorCode.THERE_IS_NO_USER));

        var boardEntity = boardRepository.findById(postRequest.getBoardId())
                .orElseThrow(() -> new BusinessException(BoardErrorCode.THERE_IS_NO_BOARD));

        var entity = PostEntity.builder()
                .postTitle(postRequest.getPostTitle())
                .postContent(postRequest.getPostContent())
                .userId(user.getId())
                .board(boardEntity)
                .thumbnailUrl(postRequest.getPostImageList().isEmpty() ? null : postRequest.getPostImageList().get(0))
                .images(postRequest.getPostImageList().isEmpty() ? null : String.join(",", postRequest.getPostImageList()))
                .createdBy(Constant.ADMIN.getMessage())
                .createdAt(LocalDateTime.now())
                .updatedBy(Constant.ADMIN.getMessage())
                .updatedAt(LocalDateTime.now())
                .build();
        var saveEntity = postRepository.save(entity);

        return postConverter.toDto(saveEntity, false);
    }

    public PostDto getPost(Long id) {
        Optional<PostEntity> postEntityOptional = postRepository.findById(id);
        PostEntity post = postEntityOptional.orElseThrow(
                () -> new BusinessException(PostErrorCode.THERE_IS_NO_POST));
        return postConverter.toDto(post, true);
    }

    public PostDto updatePost(
            PostRequest postRequest
    ) {
        Long postId = postRequest.getId();
        var postEntityOptional = postRepository.findById(postId);
        var entity = postEntityOptional.orElseThrow(
                () -> new BusinessException(PostErrorCode.THERE_IS_NO_POST));
        var dto = postConverter.toDto(entity, false);
        dto.setPostTitle(postRequest.getPostTitle());
        dto.setPostContent(postRequest.getPostContent());
        dto.setUpdatedBy(Constant.ADMIN.getMessage());
        dto.setUpdatedAt(LocalDateTime.now());
        var saveEntity = postConverter.toEntity(dto);
        postRepository.save(saveEntity);
        return dto;
    }

    public void deletePost(
            Long id
    ) {
        postRepository.deleteById(id);
    }

    public Api<List<PostDto>> getPostList(Pageable pageable) {

        var entityList = postRepository.findAll(pageable);

        var pagination = Pagination.builder()
                .page(entityList.getNumber())
                .currentElements(entityList.getNumberOfElements())
                .size(entityList.getSize())
                .totalElements(entityList.getTotalElements())
                .totalPage(entityList.getTotalPages())
                .build();

        var dtoList = entityList.stream()
                .map((PostEntity postEntity) -> postConverter.toDto(postEntity, false))
                .toList();

        return Api.<List<PostDto>>builder()
                .body(dtoList)
                .pagination(pagination)
                .build()
                ;
    }

    public boolean isWriter(String userEmail, Long postId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException(PostErrorCode.THERE_IS_NO_POST)
        );

        UserEntity user = userRepository.findByEmailAddr(userEmail).orElseThrow(
                () -> new BusinessException(UserErrorCode.THERE_IS_NO_USER)
        );

        return Objects.equals(user.getId(), post.getUserId());
    }


    // 게시글 좋아요 상태 업데이트
    public PostLikeDto updatePostLike(PostLikeRequest postLikeRequest) {
        boolean isLike = postLikeRequest.isLike();

        PostLikePK postLikePK = new PostLikePK();
        postLikePK.setPostId(postLikeRequest.getPostId());
        postLikePK.setUserId(postLikeRequest.getUserId());

        var entity = PostLikeEntity.builder()
                .postLikePK(postLikePK)
                .createdBy(postLikeRequest.getUserId().toString())
                .createdAt(LocalDateTime.now())
                .updatedBy(postLikeRequest.getUserId().toString())
                .updatedAt(LocalDateTime.now())
                .build();

        PostLikeDto likeDto = postConverter.toLikeDto(entity);

        if (isLike) {
            postLikeRepository.save(entity);
        } else {
            postLikeRepository.delete(entity);
        }
        return likeDto;
    }
}