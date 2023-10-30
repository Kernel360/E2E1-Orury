package com.kernel360.orury.domain.user.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByEmailAddr(String email);

	UserEntity findByUserNickname(String name);
}
