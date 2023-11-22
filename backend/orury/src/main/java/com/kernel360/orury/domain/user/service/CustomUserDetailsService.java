package com.kernel360.orury.domain.user.service;

import com.kernel360.orury.domain.user.db.UserEntity;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.global.error.code.UserErrorCode;
import com.kernel360.orury.global.error.exception.BusinessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String emailAddr) {
		return userRepository.findOneWithAuthoritiesByEmailAddr(emailAddr)
			.map(user -> createUser(emailAddr, user))
			.orElseThrow(() -> new BusinessException(UserErrorCode.THERE_IS_NO_USER));
	}

	private org.springframework.security.core.userdetails.User createUser(String emailAddr, UserEntity user) {
		if (!user.isActivated()) {
			throw new BusinessException(UserErrorCode.NOT_ACTIVATED);
		}

		List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
			.map(authority -> new SimpleGrantedAuthority(authority.getName()))
			.collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(user.getEmailAddr(),
			user.getPassword(),
			grantedAuthorities);
	}
}
