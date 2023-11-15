package com.kernel360.orury.domain.post.service;

import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.comment.model.CommentDto;
import com.kernel360.orury.domain.comment.service.CommentConverter;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.user.db.UserEntity;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.global.message.errors.ErrorMessages;
import lombok.RequiredArgsConstructor;
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

    public PostDto toDto(PostEntity postEntity) {

        var commentList = postEntity.getCommentList()
                .stream()
                .map(commentConverter::toDto)
                .toList();

        // map으로 변환
        Map<String, List<CommentDto>> commentMap = new HashMap<>();

        for (CommentDto comment : commentList) {

            Long pid = comment.getPId();

            if (pid == null) {
                commentMap.computeIfAbsent("0", k -> new ArrayList<>()).add(comment);

            } else {
                // pid가 있는 경우
                commentMap.computeIfAbsent(pid.toString(), k -> new ArrayList<>()).add(comment);
            }
        }


        // userNickname 설정 로직 추가
        UserEntity userEntity = userRepository.findById(postEntity.getUserId())
                .orElseThrow(() -> new RuntimeException(ErrorMessages.THERE_IS_NO_USER.getMessage() + postEntity.getUserId()));

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
                    .collect(Collectors.toList()))
                .commentList(commentList)
                .commentMap(commentMap)
                .createdBy(postEntity.getCreatedBy())
                .createdAt(postEntity.getCreatedAt())
                .updatedBy(postEntity.getUpdatedBy())
                .updatedAt(postEntity.getUpdatedAt())
                .build();
    }

    public PostEntity toEntity(PostDto postDto) {
        BoardEntity boardEntity = boardRepository.findById(postDto.getBoardId())
                .orElseThrow(
                        () -> new RuntimeException(ErrorMessages.THERE_IS_NO_BOARD.getMessage() + postDto.getBoardId())
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
}
