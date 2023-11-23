package com.kernel360.orury.domain.post.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.comment.db.CommentRepository;
import com.kernel360.orury.domain.comment.model.CommentDto;
import com.kernel360.orury.domain.comment.service.CommentConverter;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.db.PostLikeEntity;
import com.kernel360.orury.domain.post.db.PostLikeRepository;
import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.post.model.PostLikeDto;
import com.kernel360.orury.domain.user.db.UserEntity;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.global.error.code.BoardErrorCode;
import com.kernel360.orury.global.error.code.UserErrorCode;
import com.kernel360.orury.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.getenv;

@Service
@RequiredArgsConstructor
public class PostConverter {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentConverter commentConverter;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    public PostDto toDto(PostEntity postEntity, boolean bool) {
        Map<String, List<CommentDto>> commentMap = Map.of();
        List<CommentDto> commentList = List.of();
        if (bool) {
            commentList = commentRepository.findAllByPostIdOrderByIdDesc(postEntity.getId())
                .stream()
                .map(commentConverter::toDto)
                .toList();

            // map으로 변환
            commentMap = new HashMap<>();
            for (CommentDto comment : commentList) {
                Long pid = comment.getPId();
                if (pid == null) {
                    commentMap.computeIfAbsent("0", k -> new ArrayList<>()).add(comment);
                } else {
                    // pid가 있는 경우
                    commentMap.computeIfAbsent(pid.toString(), k -> new ArrayList<>()).add(comment);
                }
            }
        }

        Long commentCnt = commentRepository.countByPostId(postEntity.getId());

        // 게시글 작성자 userNickname 설정을 위한 entity
        UserEntity userEntity = userRepository.findById(postEntity.getUserId())
            .orElseThrow(() -> new BusinessException(UserErrorCode.THERE_IS_NO_USER));

        // 좋아요 수 및 유저 좋아요 세팅
        long loginId = getLoginId();
        boolean isLike = getPostLike(loginId, postEntity.getId());
        Long likeCnt = postLikeRepository.countByPostLikePKPostId(postEntity.getId());

        return PostDto.builder()
            .id(postEntity.getId())
            .boardId(postEntity.getBoard().getId())
            .postTitle(postEntity.getPostTitle())
            .postContent(postEntity.getPostContent())
            .userNickname(userEntity.getNickname())
            .viewCnt(postEntity.getViewCnt())
            .likeCnt(postEntity.getLikeCnt())
            .userId(postEntity.getUserId())
            .thumbnailUrl(postEntity.getThumbnailUrl())
            .imageList(postEntity.getImages() == null ? List.of() : Arrays.stream(postEntity.getImages().split(","))
                .map(image -> getenv().get("IMGUR_URL") + image)
                .collect(Collectors.toList())
            )
            .commentCnt(commentCnt)
            .commentMap(commentMap)
            .isLike(isLike)
            .likeCnt(likeCnt.intValue())
            .createdBy(postEntity.getCreatedBy())
            .createdAt(postEntity.getCreatedAt())
            .updatedBy(postEntity.getUpdatedBy())
            .updatedAt(postEntity.getUpdatedAt())
            .build();
    }

    public PostEntity toEntity(PostDto postDto) {
        BoardEntity boardEntity = boardRepository.findById(postDto.getBoardId())
            .orElseThrow(
                () -> new BusinessException(BoardErrorCode.THERE_IS_NO_BOARD)
            );

        return PostEntity.builder()
            .id(postDto.getId())
            .board(boardEntity)
            .postTitle(postDto.getPostTitle())
            .postContent(postDto.getPostContent())
            .viewCnt(postDto.getViewCnt())
            .likeCnt(postDto.getLikeCnt())
            .userId(postDto.getUserId())
            .thumbnailUrl(postDto.getThumbnailUrl())
            .images(postDto.getImageList().equals(List.of()) ? null : postDto.getImageList()
                .stream()
                .map(s -> s.replaceFirst(getenv().get("IMGUR_URL"), ""))
                .collect(Collectors.joining(",")))
            .createdBy(postDto.getCreatedBy())
            .createdAt(postDto.getCreatedAt())
            .updatedBy(postDto.getUpdatedBy())
            .updatedAt(postDto.getUpdatedAt())
            .build();
    }

    public PostLikeDto toLikeDto(PostLikeEntity postLikeEntity) {
        return PostLikeDto.builder()
            .postId(postLikeEntity.getPostLikePK().getPostId())
            .userId(postLikeEntity.getPostLikePK().getUserId())
            .build();
    }

    // 게시글 좋아요 여부값 가져오기
    private boolean getPostLike(long loginId, long postId) {
        // 게시글 좋아요 테이블 안에 값이 있으면 좋아요, 없으면 좋아요 아님
        return postLikeRepository.findByPostLikePKUserIdAndPostLikePKPostId(loginId, postId) != null;
    }

    // 로그인 유저 id 가져오기
    private long getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginEmail = authentication.getName();
        UserEntity loginUser = userRepository.findByEmailAddr(loginEmail)
            .orElseThrow(() -> new BusinessException(UserErrorCode.THERE_IS_NO_USER));
        return loginUser.getId();
    }

    // 공지 게시글 Dto
    public PostDto toNoticeDto(PostEntity postEntity) {
        return PostDto.builder()
            .id(postEntity.getId())
            .postTitle(postEntity.getPostTitle())
            .postContent(postEntity.getPostContent())
            .build();
    }
}

