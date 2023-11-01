package com.kernel360.orury.domain.post.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.comment.db.CommentEntity;
import com.kernel360.orury.global.common.BaseEntity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import java.util.List;

@Getter
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
	private boolean isDelete;
	private Long userId;

	@OneToMany(mappedBy = "post")
	@Builder.Default
	@Where(clause = "is_delete = false")
	@OrderBy(clause = "id desc")
	private List<CommentEntity> commentList = List.of();
}