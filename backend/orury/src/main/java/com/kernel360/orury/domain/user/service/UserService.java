// package com.kernel360.orury.domain.user.service;
//
// import java.time.LocalDateTime;
//
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;
//
// import com.kernel360.orury.domain.user.db.UserEntity;
// import com.kernel360.orury.domain.user.db.UserRepository;
// import com.kernel360.orury.domain.user.model.UserRegisterDto;
// import com.kernel360.orury.domain.user.model.UserResponseDto;
//
// import lombok.RequiredArgsConstructor;
//
// @Service
// @RequiredArgsConstructor
// public class UserService {
//
// 	private final UserRepository userRepository;
// 	private final UserConverter userConverter;
// 	private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
// 	public UserResponseDto join(UserRegisterDto userRegisterDto) {
// 		if (userRepository.findByEmailAddr(userRegisterDto.getEmailAddr()) != null) {
// 			throw new AlreadyRegisteredUserException();
// 		}
// 		String rawPasswd = userRegisterDto.getPasswd();
// 		String encPasswd = bCryptPasswordEncoder.encode(rawPasswd);
//
// 		var newUser = UserEntity.builder()
// 			.emailAddr(userRegisterDto.getEmailAddr())
// 			.userNickname(userRegisterDto.getUserNickname())
// 			.passwd(encPasswd)
// 			.isWithdrawl(false)
// 			.passwdUpdateDate(LocalDateTime.now())
// 			.createdBy("admin")
// 			.createdAt(LocalDateTime.now())
// 			.updatedBy("admin")
// 			.updatedAt(LocalDateTime.now())
// 			.build();
//
// 		var saveEntity = userRepository.save(newUser);
//
// 		return userConverter.toResponseDto(saveEntity);
//
// 	}
//
// }
