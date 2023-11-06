package com.kernel360.orury.domain.user.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kernel360.orury.domain.user.db.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {

	private Long id;

	@NotNull
	private String emailAddr;
	@NotNull
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private String nickname;
	private LocalDateTime passwdUpdateDate;
	private Boolean isWithdrawal;
	private String withdrawalId;
	private LocalDateTime withdrawalAt;
	private String remark1;
	private String remark2;
	private String remark3;
	private String createdBy;
	private LocalDateTime createdAt;
	private String updatedBy;
	private LocalDateTime updatedAt;
	private Set<AuthorityDto> authorityDtoSet;

	public static UserDto from(UserEntity user) {
		if (user == null)
			return null;

		return UserDto.builder()
			.emailAddr(user.getEmailAddr())
			.authorityDtoSet(user.getAuthorities().stream()
				.map(authority -> AuthorityDto.builder().name(authority.getName()).build())
				.collect(Collectors.toSet()))
			.build();
	}
}
