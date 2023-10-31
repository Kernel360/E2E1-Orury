package com.kernel360.orury.domain.user.db;

import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByEmailAddr(String emailAddr);

	Optional<UserEntity> findByUserNickname(String emailAddr);

}
