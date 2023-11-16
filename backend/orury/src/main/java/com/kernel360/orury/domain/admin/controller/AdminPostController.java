package com.kernel360.orury.domain.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kernel360.orury.domain.admin.service.AdminPostService;
import com.kernel360.orury.domain.board.model.BoardDto;
import com.kernel360.orury.domain.board.model.BoardRequest;
import com.kernel360.orury.domain.board.service.BoardService;
import com.kernel360.orury.domain.post.model.PostDto;
import com.kernel360.orury.domain.post.model.PostRequest;
import com.kernel360.orury.domain.post.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPostController {

	public final AdminPostService adminPostService;

	@GetMapping("/post")
	public String post(Model model) {
		List<PostDto> postList = adminPostService.getPostList();
		model.addAttribute("postList", postList);
		return "post-table";
	}

	@PostMapping("/deletePost")
	public String deletePost(@RequestParam("postId") Long postId) {
		adminPostService.deletePost(postId);
		return "redirect:/admin/post";
	}

	@PostMapping("/post")
	public String createPost(@ModelAttribute PostRequest postRequest) {
		adminPostService.createPost(postRequest);
		return "redirect:/admin/post";
	}
}
