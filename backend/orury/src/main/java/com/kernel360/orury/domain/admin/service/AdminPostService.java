package com.kernel360.orury.domain.admin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.db.PostRepository;
import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.post.model.PostRequest;
import com.kernel360.orury.domain.post.service.PostConverter;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.global.constants.Constant;
import com.kernel360.orury.global.message.errors.ErrorMessages;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminPostService {

	private final PostRepository postRepository;
	private final BoardRepository boardRepository;
	private final PostConverter postConverter;
	private final UserRepository userRepository;

	public PostDto createPost(
		PostRequest postRequest
	) {
		var boardEntity = boardRepository.findById(postRequest.getBoardId())
			.orElseThrow(() -> new RuntimeException(ErrorMessages.THERE_IS_NO_BOARD.getMessage()));

		var entity = PostEntity.builder()
			.postTitle(postRequest.getPostTitle())
			.postContent(postRequest.getPostContent())
			.userId(1L)
			.board(boardEntity)
			.createdBy(Constant.ADMIN.getMessage())
			.createdAt(LocalDateTime.now())
			.updatedBy(Constant.ADMIN.getMessage())
			.updatedAt(LocalDateTime.now())
			.build();
		var saveEntity = postRepository.save(entity);

		return postConverter.toDto(saveEntity);
	}

	public PostDto getPost(Long id) {
		Optional<PostEntity> postEntityOptional = postRepository.findById(id);
		PostEntity post = postEntityOptional.orElseThrow(
			() -> new RuntimeException(ErrorMessages.THERE_IS_NO_POST.getMessage() + id));
		return postConverter.toDto(post);
	}

	public PostDto updatePost(
		PostRequest postRequest
	) {
		Long postId = postRequest.getId();
		var postEntityOptional = postRepository.findById(postId);
		var entity = postEntityOptional.orElseThrow(
			() -> new RuntimeException(ErrorMessages.THERE_IS_NO_POST.getMessage() + postId));
		var dto = postConverter.toDto(entity);
		dto.setPostTitle(postRequest.getPostTitle());
		dto.setPostContent(postRequest.getPostContent());
		dto.setUpdatedBy(Constant.ADMIN.getMessage());
		dto.setUpdatedAt(LocalDateTime.now());
		var saveEntity = postConverter.toEntity(dto);
		postRepository.save(saveEntity);
		return dto;
	}

	public void deletePost(
		Long id
	) {
		postRepository.deleteById(id);
	}

	public List<PostDto> getPostList() {

		var entityList = postRepository.findAll();

		return entityList.stream()
			.map(postConverter::toDto)
			.toList();
	}
}

