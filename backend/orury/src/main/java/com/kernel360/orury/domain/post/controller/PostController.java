package com.kernel360.orury.domain.post.controller;

import com.kernel360.orury.domain.post.model.PostViewRequest;
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

	@PostMapping("")
	public PostDto create(
		@Valid
		@RequestBody
		PostRequest postRequest
	) {
		return postService.createPost(postRequest);
	}

	@PostMapping("/view")
	public PostDto view(
		@Valid
		@RequestBody PostViewRequest postViewRequest
	) {
		return postService.getPost(postViewRequest);
	}

	@GetMapping("/all")
	public List<PostDto> list(

	) {
		return postService.all();
	}

	@PatchMapping("")
	public PostDto updatePost(
		@Valid
		@RequestBody
		PostRequest postRequest
	) {
		return postService.updatePost(postRequest);
	}

	@DeleteMapping("/{postId}")
	public void delete(
		@PathVariable
		Long postId
	) {
		postService.deletePost(postId);
	}
}
