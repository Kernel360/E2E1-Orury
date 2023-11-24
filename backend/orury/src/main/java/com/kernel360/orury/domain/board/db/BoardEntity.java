package com.kernel360.orury.domain.board.db;

import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.global.common.BaseEntity;
import com.kernel360.orury.global.common.Listener;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import java.util.List;

/**
 * author : hyungjoon cho
 * date : 2023/10/24
 * description : 게시판 엔티티
 */
@ToString
@Entity(name = "board")
@EntityListeners(Listener.class)
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BoardEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String boardTitle;

//	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@Builder.Default
//	@OrderBy(clause = "id desc")
//	private List<PostEntity> postList = List.of();
}
