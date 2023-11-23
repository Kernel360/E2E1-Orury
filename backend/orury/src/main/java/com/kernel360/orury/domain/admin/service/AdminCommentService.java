package com.kernel360.orury.domain.admin.service;

import com.kernel360.orury.domain.comment.db.CommentEntity;
import com.kernel360.orury.domain.comment.db.CommentRepository;
import com.kernel360.orury.domain.comment.model.CommentDto;
import com.kernel360.orury.domain.comment.model.CommentRequest;
import com.kernel360.orury.domain.comment.service.CommentConverter;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.db.PostRepository;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.global.constants.Constant;
import com.kernel360.orury.global.error.code.PostErrorCode;
import com.kernel360.orury.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCommentService {
	private final CommentRepository commentRepository;
	private final CommentConverter commentConverter;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	public CommentDto createComment(CommentRequest commentRequest) {
		PostEntity postEntity = postRepository.findById(commentRequest.getPostId())
			.orElseThrow(
				() -> new BusinessException(PostErrorCode.THERE_IS_NO_POST)
			);

		CommentEntity commentEntity = CommentEntity.builder()
			.userId(commentRequest.getUserId())
			.post(postEntity)
			.commentContent(commentRequest.getCommentContent())
			// 대댓글과 본댓글 판별
			.pId(commentRequest.getId() == null ? null : commentRequest.getId())
			.createdBy(Constant.ADMIN.getMessage())
			.createdAt(LocalDateTime.now())
			.updatedBy(Constant.ADMIN.getMessage())
			.updatedAt(LocalDateTime.now())
			.build();

		CommentEntity saveEntity = commentRepository.save(commentEntity);

		return commentConverter.toDto(saveEntity);
	}

	public List<CommentDto> getCommentList() {

		List<CommentEntity> entityList = commentRepository.findAll();

		return entityList.stream()
			.map(commentConverter::toDto)
			.toList();
	}

	public void deleteComment(Long commentId) {
		commentRepository.deleteById(commentId);
	}

}