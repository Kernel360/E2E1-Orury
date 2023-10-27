package com.kernel360.orury.domain.post.controller;

import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.post.model.PostRequest;
import com.kernel360.orury.domain.post.service.PostService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
		return postService.createPost(postRequest);
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
	public List<PostDto> getPostList() {
		return postService.getPostList();
	}

	// 게시글 수정
	@PatchMapping("")
	public PostDto updatePost(
		@Valid
		@RequestBody
		PostRequest postRequest
	) {
		return postService.updatePost(postRequest);
	}

	// 게시글 삭제
	@DeleteMapping("/{id}")
	public void deletePost(@PathVariable Long id) {
		postService.deletePost(id);
	}
}