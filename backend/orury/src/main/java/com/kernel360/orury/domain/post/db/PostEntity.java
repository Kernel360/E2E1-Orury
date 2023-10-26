package com.kernel360.orury.domain.post.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.global.domain.BaseEntity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Entity(name = "post")

public class PostEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JsonIgnore
	@ToString.Exclude
	private BoardEntity board;

	private String postTitle;
	@Column(columnDefinition = "TEXT")
	private String postContent;
	private String userNickname;
	private int viewCnt;
	private int likeCnt;
	// @Column(columnDefinition = "BIT")
	private boolean isDelete;
	private Long userId;

	/*
	todo : 댓글이 생성되면 아래 형식 필요
	@Transient
	private List<CommentEntry> commentList = new ArrayList<>();

	 */
}