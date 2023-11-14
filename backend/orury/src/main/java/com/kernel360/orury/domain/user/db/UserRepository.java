package com.kernel360.orury.domain.user.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	@EntityGraph(attributePaths = "authorities")
	Optional<UserEntity> findOneWithAuthoritiesByEmailAddr(String username);

	Optional<UserEntity> findByEmailAddr(String userEmail);
}
