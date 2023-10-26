package com.kernel360.orury.domain.post.controller;

import com.kernel360.orury.domain.post.PostViewRequest;
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

	@PostMapping("")      // 문찬욱 : 임시 url
	public PostDto create(
		@Valid
		@RequestBody
		PostRequest postRequest
	) {
		return postService.createPost(postRequest);
	}

	// 게시글 아이돌 게시글 조회. 주석 이따 지울게요

	@PostMapping("/view")
	public PostDto view(
		@Valid
		@RequestBody PostViewRequest postViewRequest
	) {
		return postService.getPost(postViewRequest);
	}

	@GetMapping("/all")             // 문찬욱 : baordId에 따른 결과가 나오게 리팩토링 필요
	public List<PostDto> list(

	) {
		return postService.all();
	}

	@PatchMapping("")                // 문찬욱 : 임시 url, 리팩토링 필요
	public PostDto updatePost(
		@Valid
		@RequestBody
		PostRequest postRequest
	) {
		return postService.updatePost(postRequest);
	}

	@DeleteMapping("/{postId}")     // 문찬욱 : 임시 url
	public void delete(
		@PathVariable
		Long postId
	) {
		postService.deletePost(postId);
	}
}
