package com.kernel360.orury.domain.user.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {
	Optional<AuthorityEntity> findByName(String authorityName);
}
