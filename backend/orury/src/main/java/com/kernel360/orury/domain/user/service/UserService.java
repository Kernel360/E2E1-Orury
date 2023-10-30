package com.kernel360.orury.domain.user.service;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kernel360.orury.domain.user.db.UserEntity;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.domain.user.model.UserRegisterDto;
import com.kernel360.orury.domain.user.model.UserResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserConverter userConverter;

	public UserResponseDto signup(UserRegisterDto userRegisterDto) {
		if (userRepository.findByEmailAddr(userRegisterDto.getEmailAddr()) != null) {
			throw new AlreadyRegisteredUserException();
		}
		var newUser = UserEntity.builder()
			.emailAddr(userRegisterDto.getEmailAddr())
			.userNickname(userRegisterDto.getUserNickname())
			.passwd(userRegisterDto.getPasswd())
			.passwdUpdateDate(LocalDateTime.now())
			.createdBy("admin")
			.createdAt(LocalDateTime.now())
			.updatedBy("admin")
			.updatedAt(LocalDateTime.now())
			.build();

		var saveEntity = userRepository.save(newUser);

		return userConverter.toResponseDto(saveEntity);

	}

	public UserEntity findByUsername(String username) {
		return userRepository.findByUserNickname(username);
	}
}
