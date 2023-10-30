package com.kernel360.orury.domain.user.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kernel360.orury.global.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class UserEntity extends BaseEntity implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Email
	private String emailAddr;

	private String userNickname;

	private String passwd;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@LastModifiedDate
	private LocalDateTime passwdUpdateDate;
	private Boolean isWithdrawl;
	private String withdrawlId;
	private LocalDateTime withdrawlAt;
	private String remark1;
	private String remark2;
	private String remark3;
	private String authority;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton((GrantedAuthority)() -> authority);
	}

	@Override
	public String getPassword() {
		return null;
	}

	public Boolean isAdmin() {
		return authority.equals("ROLE_ADMIN");
	}

	@Override
	public String getUsername() {
		return null;
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
