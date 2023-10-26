package com.kernel360.orury.domain.post.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kernel360.orury.domain.board.db.BoardEntity;
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

	private Long boardId;

	private String postTitle;
	@Column(columnDefinition = "TEXT")
	private String postContent;
	private String userNickname;
	private int viewCnt;
	private int likeCnt;
	// @Column(columnDefinition = "BIT")
	private boolean isDelete;
	private Long userId;
	private String createdBy;
	private LocalDateTime createdAt;
	private String updatedBy;
	private LocalDateTime updatedAt;
}