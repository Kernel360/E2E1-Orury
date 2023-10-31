package com.kernel360.orury.domain.post.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.comment.db.CommentEntity;
import com.kernel360.orury.global.domain.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OrderBy;

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
	private Long userId;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	@Builder.Default
	@OrderBy(clause = "id desc")
	private List<CommentEntity> commentList = List.of();
	// 사진 url list
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	@Builder.Default
	@OrderBy(clause = "id asc")
	private List<PostImageEntity> imageList = List.of();
	private String thumbnailUrl;
}