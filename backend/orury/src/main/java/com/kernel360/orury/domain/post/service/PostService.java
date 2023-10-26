package com.kernel360.orury.domain.post.service;

import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.comment.service.CommentService;
import com.kernel360.orury.domain.post.PostViewRequest;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.db.PostRepository;
import com.kernel360.orury.domain.post.dto.PostDto;
import com.kernel360.orury.domain.post.model.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostRepository postRepository;
	private final BoardRepository boardRepository;
	private final PostConverter postConverter;
	private final CommentService commentService;

	public PostDto createPost(
		PostRequest postRequest,
		Long userId,
		Long boardId
	) {

		var entity = PostEntity.builder()
			.postTitle(postRequest.getPostTitle())
			.postContent(postRequest.getPostContent())
			.userNickname(postRequest.getUserNickname())
			.userId(userId)
			.boardId(boardId)
			.createdBy("admin")    // 문찬욱 : 임시로 "admin" 설정
			.createdAt(LocalDateTime.now())
			.updatedBy("admin")
			.updatedAt(LocalDateTime.now())
			.build();
		var saveEntity = postRepository.save(entity);
		return postConverter.toDto(saveEntity);
	}

	public PostDto getPost(PostViewRequest postViewRequest) {
		Long postId = postViewRequest.getId();
		Optional<PostEntity> postEntityOptional = postRepository.findByIdAndIsDelete(postId, false);
		PostEntity post = postEntityOptional.orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다: " + postId));
		PostDto postDto = postConverter.toDto(post);
		postDto.setCommentList(commentService.findAllByPostId(postViewRequest.getId()));

		return postDto;
	}

	public PostDto updatePost(
		PostRequest postRequest
	) {
		Long postId = postRequest.getId();
		var postEntityOptional = postRepository.findByIdAndIsDelete(postId, false);
		var entity = postEntityOptional.orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다: " + postId));
		var dto = postConverter.toDto(entity);
		dto.setPostTitle(postRequest.getPostTitle());
		dto.setPostContent(postRequest.getPostContent());
		dto.setUpdatedBy("admin");       // 문찬욱 : 임의로 "admin" 설정
		dto.setUpdatedAt(LocalDateTime.now());
		var saveEntity = postConverter.toEntity(dto);
		postRepository.save(saveEntity);
		return dto;
	}

	public void deletePost(
		Long postId
	) {
		postRepository.deleteById(postId);
	}

	public List<PostEntity> all() {
		return postRepository.findAll();
	}
}