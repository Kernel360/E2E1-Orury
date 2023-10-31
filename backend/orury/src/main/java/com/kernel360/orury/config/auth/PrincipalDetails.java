package com.kernel360.orury.config.auth;

// 시큐리티가 /login 주소로 요청이 오면 인터셉트해서 로그인을 진행시킨다
// 로그인이 완료되면 시큐리티 session을 만들어 준다 (Security ContextHolder)
// 오브젝트 타입 = Authentication 타입 객체
// Authentication 안에 User 정보가 있어야됨
// User 오브젝트 타입 = UserDetails 타입 객체

// Security Session includes Authentication includes UserDetails(=PrincipalDetails)

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kernel360.orury.domain.user.db.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrincipalDetails implements UserDetails {

	private final UserEntity userEntity;

	public PrincipalDetails(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public UserEntity getUser() {
		return userEntity;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return userEntity.getAuthorities().stream()
			.map(a -> new SimpleGrantedAuthority(a.getName()))
			.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return userEntity.getPasswd();
	}

	@Override
	public String getUsername() {
		return userEntity.getEmailAddr();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
