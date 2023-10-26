package com.kernel360.orury.domain.post.db;

import com.kernel360.orury.global.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "post")

public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String postTitle;
    @Column(columnDefinition = "TEXT")
    private String postContent;
    private String userNickname;
    private int viewCnt;
    private int likeCnt;
    private int isDelete;
    private Long userId;
    private Long boardId;
}