package com.kernel360.orury.config.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.PrincipalMethodArgumentResolver;

import com.kernel360.orury.domain.user.db.UserEntity;
import com.kernel360.orury.domain.user.db.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//시큐리티 설정에서 loginProcessingUrl("/login");으로 걸어놓음
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUserName 함수가 실행 (규칙)
@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public PrincipalDetails loadUserByUsername(String emailAddr) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmailAddr(emailAddr)
			.orElseThrow(() -> new UsernameNotFoundException("유저조회 실패"));
		return new PrincipalDetails(userEntity);
	}
}
