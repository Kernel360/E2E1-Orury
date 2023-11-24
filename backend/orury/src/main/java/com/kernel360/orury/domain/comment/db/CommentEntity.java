package com.kernel360.orury.domain.comment.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.global.common.BaseEntity;
import com.kernel360.orury.global.common.Listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

/**
 * author : hyungjoon cho
 * date : 2023/10/26
 * description : 댓글 엔티티
 */
@Entity(name = "comment")
@EntityListeners(Listener.class)
@Getter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	@ToString.Exclude
	private PostEntity post;

	@Column(columnDefinition = "TEXT")
	private String commentContent;

	private int likeCnt;

	// 부모 댓글 id, pId == null ? 본댓글 : 대댓글
	private Long pId;

}
