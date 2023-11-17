package com.kernel360.orury.domain.admin.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernel360.orury.domain.user.db.AuthorityEntity;
import com.kernel360.orury.domain.user.db.AuthorityRepository;
import com.kernel360.orury.domain.user.db.UserEntity;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.domain.user.model.UserDto;
import com.kernel360.orury.global.exception.NotFoundMemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminUserService {

	private final UserRepository userRepository;
	private final AuthorityRepository authorityRepository;

	public List<UserDto> getUserList() {

		var entityList = userRepository.findAll();

		return entityList.stream()
			.map(UserDto::from)
			.toList();
	}

	@Transactional
	public void updateAuthority(Long userId, String authorityName) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		AuthorityEntity authority = authorityRepository.findByName(authorityName)
			.orElseThrow(() -> new IllegalArgumentException("Authority not found"));

		user.getAuthorities().clear(); // 기존 권한 정보 삭제
		user.getAuthorities().add(authority); // 새로운 권한 정보 추가

		userRepository.save(user);
	}

}

