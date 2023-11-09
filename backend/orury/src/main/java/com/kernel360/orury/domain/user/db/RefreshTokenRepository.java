package com.kernel360.orury.domain.user.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUserId(Long id);

    Optional<RefreshTokenEntity> findByTokenValue(String token);
}
