package com.kernel360.orury.domain.user.service;

import org.springframework.stereotype.Service;

import com.kernel360.orury.domain.user.db.UserEntity;
import com.kernel360.orury.domain.user.model.UserRegisterDto;
import com.kernel360.orury.domain.user.model.UserResponseDto;

@Service
public class UserConverter {

	public UserResponseDto toResponseDto(UserEntity userEntity) {

		return UserResponseDto.builder()
			.id(userEntity.getId())
			.emailAddr(userEntity.getEmailAddr())
			.userNickname(userEntity.getUserNickname())
			.authority(userEntity.getAuthority())
			.build();
	}

	public UserRegisterDto toRegisterDto(UserEntity userEntity) {

		return UserRegisterDto.builder()
			.id(userEntity.getId())
			.emailAddr(userEntity.getEmailAddr())
			.userNickname(userEntity.getUserNickname())
			.passwd(userEntity.getPasswd())
			.passwdUpdateDate(userEntity.getPasswdUpdateDate())
			.isWithrawl(userEntity.getIsWithdrawl())
			.withdrawlId(userEntity.getWithdrawlId())
			.withdrawlAt(userEntity.getWithdrawlAt())
			.remark1(userEntity.getRemark1())
			.remark2(userEntity.getRemark2())
			.remark3(userEntity.getRemark3())
			.createdAt(userEntity.getCreatedAt())
			.createdBy(userEntity.getCreatedBy())
			.updatedAt(userEntity.getUpdatedAt())
			.updatedBy(userEntity.getUpdatedBy())
			.authority(userEntity.getAuthority())
			.build();
	}

	public UserEntity toEntity(UserRegisterDto dto) {

		return UserEntity.builder()
			.emailAddr(dto.getEmailAddr())
			.userNickname(dto.getUserNickname())
			.passwd(dto.getPasswd())
			.passwdUpdateDate(dto.getPasswdUpdateDate())
			.build();
	}

}
