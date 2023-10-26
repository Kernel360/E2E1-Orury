package com.kernel360.orury.domain.post.service;

import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.dto.PostDto;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class PostConverter {

    public PostDto toDto(PostEntity postEntity){
        return PostDto.builder()
                .id(postEntity.getId())
                .postTitle(postEntity.getPostTitle())
                .postContent(postEntity.getPostContent())
                .userNickname(postEntity.getUserNickname())
                .viewCnt(postEntity.getViewCnt())
                .likeCnt(postEntity.getLikeCnt())
                .isDelete(postEntity.getIsDelete())
                .userId(postEntity.getUserId())
                .boardId(postEntity.getBoardId())
                .build();
    }

    public PostEntity toEntity(PostDto postDto){
        return PostEntity.builder()
                .id(postDto.getId())
                .postTitle()
                .build();
    }
}
