package com.kernel360.orury.domain.post.service;

import com.kernel360.orury.domain.board.db.BoardRepository;
import com.kernel360.orury.domain.comment.service.CommentService;
import com.kernel360.orury.domain.post.db.PostImageEntity;
import com.kernel360.orury.domain.post.db.PostImageRepository;
import com.kernel360.orury.domain.post.db.PostEntity;
import com.kernel360.orury.domain.post.db.PostRepository;
import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.post.model.PostRequest;

import com.kernel360.orury.global.common.Api;
import com.kernel360.orury.global.common.Pagination;
import com.kernel360.orury.global.constants.Constant;
import com.kernel360.orury.global.message.errors.ErrorMessages;
import com.kernel360.orury.global.message.info.InfoMessages;
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
	private final PostImageRepository postImageRepository;

	public PostDto createPost(
		PostRequest postRequest
	) {

		var boardEntity = boardRepository.findById(postRequest.getBoardId())
			.orElseThrow(() -> new RuntimeException(ErrorMessages.THERE_IS_NO_BOARD.getMessage()));

		var entity = PostEntity.builder()
			.postTitle(postRequest.getPostTitle())
			.postContent(postRequest.getPostContent())
			.userNickname(postRequest.getUserNickname())
			.userId(postRequest.getUserId())
			.board(boardEntity)
			.thumbnailUrl(postRequest.getPostImageList().isEmpty() ? null : postRequest.getPostImageList().get(0))
			.createdBy(Constant.ADMIN.getMessage())
			.createdAt(LocalDateTime.now())
			.updatedBy(Constant.ADMIN.getMessage())
			.updatedAt(LocalDateTime.now())
			.build();
		var saveEntity = postRepository.save(entity);

		// 사진 이미지 저장
		try {
			if(!postRequest.getPostImageList().isEmpty()) {
				for (String url : postRequest.getPostImageList()) {
					PostImageEntity postImageEntity = PostImageEntity.builder()
							.imageUrl(url)
							.post(entity)
							.createdBy(Constant.ADMIN.getMessage())
							.createdAt(LocalDateTime.now())
							.updatedBy(Constant.ADMIN.getMessage())
							.updatedAt(LocalDateTime.now())
							.build();
					postImageRepository.save(postImageEntity);
				}
			}

		} catch (Exception e) {
			throw e;
		}



		return postConverter.toDto(saveEntity);
	}

	public PostDto getPost(Long id) {
		Optional<PostEntity> postEntityOptional = postRepository.findById(id);
		PostEntity post = postEntityOptional.orElseThrow(() -> new RuntimeException(ErrorMessages.THERE_IS_NO_POST.getMessage() + id));
		return postConverter.toDto(post);
	}

	public PostDto updatePost(
		PostRequest postRequest
	) {
		Long postId = postRequest.getId();
		var postEntityOptional = postRepository.findById(postId);
		var entity = postEntityOptional.orElseThrow(() -> new RuntimeException(ErrorMessages.THERE_IS_NO_POST.getMessage() + postId));
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
		log.info(InfoMessages.POST_DELETED.getMessage() + id);
	}

	public Api<List<PostDto>> getPostList(Pageable pageable) {

		var entityList =  postRepository.findAll(pageable);

		var pagination = Pagination.builder()
			.page(entityList.getNumber())
			.currentElements(entityList.getNumberOfElements())
			.size(entityList.getSize())
			.totalElements(entityList.getTotalElements())
			.totalPage(entityList.getTotalPages())
			.build()
			;

		var dtoList = entityList.stream()
			.map(postConverter::toDto)
			.toList();

		return  Api.<List<PostDto>>builder()
			.body(dtoList)
			.pagination(pagination)
			.build()
			;
	}

}