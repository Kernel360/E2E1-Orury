package com.kernel360.orury.domain.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kernel360.orury.domain.admin.service.AdminUserService;
import com.kernel360.orury.domain.user.model.UserDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminUserController {

	private final AdminUserService adminUserService;

	@GetMapping("/user")
	public String user(Model model) {
		List<UserDto> userList = adminUserService.getUserList();
		model.addAttribute("userList", userList);

		return "user-table";
	}

	@PostMapping("/authority")
	public String authority(@RequestParam Long userId, @RequestParam String authority) {
		adminUserService.updateAuthority(userId, authority);
		return "redirect:/admin/user";
	}

}
