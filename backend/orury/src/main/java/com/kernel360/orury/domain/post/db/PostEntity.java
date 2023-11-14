package com.kernel360.orury.domain.post.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kernel360.orury.domain.board.db.BoardEntity;
import com.kernel360.orury.domain.comment.db.CommentEntity;
import com.kernel360.orury.global.common.BaseEntity;
import com.kernel360.orury.global.common.Listener;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@Entity(name = "post")
@EntityListeners(Listener.class)
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
	@Size(max=255)
	private String thumbnailUrl;
	@Size(max=255)
	private String images;
	private int viewCnt;
	private int likeCnt;
	private Long userId;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	@Builder.Default
	@OrderBy(clause = "id desc")
	private List<CommentEntity> commentList = List.of();
}