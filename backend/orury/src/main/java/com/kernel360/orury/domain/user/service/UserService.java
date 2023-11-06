package com.kernel360.orury.domain.user.service;

import java.time.LocalDateTime;
import java.util.Collections;

import com.kernel360.orury.domain.user.db.AuthorityEntity;
import com.kernel360.orury.global.constants.Constant;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernel360.orury.domain.user.db.UserEntity;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.domain.user.exception.DuplicateMemberException;
import com.kernel360.orury.domain.user.exception.NotFoundMemberException;
import com.kernel360.orury.domain.user.model.UserDto;
import com.kernel360.orury.global.common.SecurityUtil;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public UserDto signup(UserDto userDto) {
		if (userRepository.findOneWithAuthoritiesByEmailAddr(userDto.getEmailAddr()).orElse(null) != null) {
			throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
		}

		AuthorityEntity authority = AuthorityEntity.builder()
			.name(Constant.ROLE_USER.getMessage())
			.build();

		UserEntity user = UserEntity.builder()
			.emailAddr(userDto.getEmailAddr())
			.password(passwordEncoder.encode(userDto.getPassword()))
			.authorities(Collections.singleton(authority))
			.createdAt(LocalDateTime.now())
			.createdBy(Constant.SYSTEM.getMessage())
			.updatedAt(LocalDateTime.now())
			.updatedBy(Constant.SYSTEM.getMessage())
			.activated(true)
			.build();

		return UserDto.from(userRepository.save(user));
	}

	@Transactional(readOnly = true)
	public UserDto getUserWithAuthorities(String username) {
		return UserDto.from(userRepository.findOneWithAuthoritiesByEmailAddr(username).orElse(null));
	}

	@Transactional(readOnly = true)
	public UserDto getMyUserWithAuthorities() {
		return UserDto.from(
			SecurityUtil.getCurrentUsername()
				.flatMap(userRepository::findOneWithAuthoritiesByEmailAddr)
				.orElseThrow(() -> new NotFoundMemberException("Member not found"))
		);
	}
}