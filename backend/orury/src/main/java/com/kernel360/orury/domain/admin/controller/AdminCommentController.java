package com.kernel360.orury.domain.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kernel360.orury.domain.admin.service.AdminCommentService;
import com.kernel360.orury.domain.comment.model.CommentDto;
import com.kernel360.orury.domain.comment.model.CommentRequest;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCommentController {

	private final AdminCommentService adminCommentService;

	@GetMapping("/comment")
	public String comment(Model model) {

		List<CommentDto> commentList = adminCommentService.getCommentList();
		model.addAttribute("commentList", commentList);
		return "comment-table";
	}

	@PostMapping("/comment")
	public String createComment(@ModelAttribute CommentRequest commentRequest) {
		adminCommentService.createComment(commentRequest);
		return "redirect:/admin/comment";
	}

	@PostMapping("/deleteComment")
	public String deleteComment(@RequestParam("commentId") Long commentId) {
		adminCommentService.deleteComment(commentId);
		return "redirect:/admin/comment";
	}

}
