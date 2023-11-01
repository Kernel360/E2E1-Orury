package com.kernel360.orury.domain.board.db;

import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.global.common.BaseEntity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import java.util.List;

/**
 * author : hyungjoon cho
 * date : 2023/10/24
 * description : 게시판 엔티티
 */
@ToString
@Entity(name = "board")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String boardTitle;

	@OneToMany(mappedBy = "board")
	@Builder.Default
	@Where(clause = "is_delete = false")
	@OrderBy(clause = "id desc")
	private List<PostEntity> postList = List.of();
}
