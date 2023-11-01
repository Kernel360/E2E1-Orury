package com.kernel360.orury.domain.post.service;

import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.comment.service.CommentService;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.db.PostRepository;
import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.post.model.PostRequest;

import com.kernel360.orury.global.common.Api;
import com.kernel360.orury.global.common.Pagination;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
	private final PostRepository postRepository;
	private final BoardRepository boardRepository;
	private final PostConverter postConverter;
	private final CommentService commentService;

	private static final String ADMIN = "admin";

	public PostDto createPost(
		PostRequest postRequest
	) {

		var boardEntity = boardRepository.findById(postRequest.getBoardId())
			.orElseThrow(() -> new RuntimeException("해당 게시판이 없습니다"));

		var entity = PostEntity.builder()
			.postTitle(postRequest.getPostTitle())
			.postContent(postRequest.getPostContent())
			.userNickname(postRequest.getUserNickname())
			.userId(postRequest.getUserId())
			.board(boardEntity)
			.createdBy(ADMIN)
			.createdAt(LocalDateTime.now())
			.updatedBy(ADMIN)
			.updatedAt(LocalDateTime.now())
			.build();
		var saveEntity = postRepository.save(entity);
		return postConverter.toDto(saveEntity);
	}

	public PostDto getPost(Long id) {
		Optional<PostEntity> postEntityOptional = postRepository.findByIdAndIsDelete(id, false);
		PostEntity post = postEntityOptional.orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다: " + id));
		return postConverter.toDto(post);
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
		dto.setUpdatedBy(ADMIN);
		dto.setUpdatedAt(LocalDateTime.now());
		var saveEntity = postConverter.toEntity(dto);
		postRepository.save(saveEntity);
		return dto;
	}

	public void deletePost(
		Long id
	) {
		postRepository.deleteById(id);
		log.info("게시글이 삭제되었습니다. : {}", id);
	}

	public Api<List<PostDto>> getPostList(Pageable pageable) {

		var entityList = postRepository.findAll(pageable);

		var pagination = Pagination.builder()
			.page(entityList.getNumber())
			.currentElements(entityList.getNumberOfElements())
			.size(entityList.getSize())
			.totalElements(entityList.getTotalElements())
			.totalPage(entityList.getTotalPages())
			.build();

		var dtoList = entityList.stream()
			.map(postConverter::toDto)
			.toList();

		return Api.<List<PostDto>>builder()
			.body(dtoList)
			.pagination(pagination)
			.build()
			;
	}

}