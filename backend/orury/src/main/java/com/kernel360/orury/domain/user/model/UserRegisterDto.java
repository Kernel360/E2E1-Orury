package com.kernel360.orury.domain.user.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRegisterDto {

	private Long id;

	@NotBlank
	private String emailAddr;
	@NotBlank
	private String userNickname;
	@NotBlank
	private String passwd;
	private LocalDateTime passwdUpdateDate;
	private Boolean isWithrawl;
	private String withdrawlId;
	private LocalDateTime withdrawlAt;
	private String remark1;
	private String remark2;
	private String remark3;
	private String createdBy;
	private LocalDateTime createdAt;
	private String updatedBy;
	private LocalDateTime updatedAt;
	private String authorities;

}
