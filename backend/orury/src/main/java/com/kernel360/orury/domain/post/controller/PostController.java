package com.kernel360.orury.domain.post.controller;

import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.post.model.PostRequest;
import com.kernel360.orury.domain.post.service.PostService;

import com.kernel360.orury.global.common.Api;

import com.kernel360.orury.global.message.errors.ErrorMessages;
import com.kernel360.orury.global.message.info.InfoMessages;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;

	// 게시글 생성
	@PostMapping("")
	public PostDto createPost(
		@Valid
		@RequestBody
		PostRequest postRequest
	) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();

		return postService.createPost(postRequest, userEmail);
	}

	// 특정 게시글 조회
	@GetMapping("/{id}")
	public PostDto getPost(
		@PathVariable Long id
	) {
		return postService.getPost(id);
	}

	// 게시글 모두 조회
	@GetMapping("/all")
	public Api<List<PostDto>> getPostList(
		@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		return postService.getPostList(pageable);
	}

	// 게시글 수정
	@PatchMapping("")
	public ResponseEntity<String> updatePost(
		@Valid
		@RequestBody
		PostRequest postRequest
	) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();

		if(postService.isWriter(userEmail, postRequest.getId())){
			var postEntity = postService.updatePost(postRequest);
			return ResponseEntity.ok(InfoMessages.POST_CREATED.getMessage() + postEntity.getId().toString());
		}
		else
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
	}
	// 게시글 삭제
	@DeleteMapping("/{post_id}")
	public ResponseEntity<String> deletePost(@PathVariable Long post_id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = authentication.getName();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		boolean isAdmin = authorities.stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

		if(postService.isWriter(userEmail, post_id) || isAdmin ){
			postService.deletePost(post_id);
			return ResponseEntity.ok(InfoMessages.POST_DELETED.getMessage() + post_id);
		}else{
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 "+ ErrorMessages.THERE_IS_NO_AUTHORITY.getMessage());
		}
	}
}
