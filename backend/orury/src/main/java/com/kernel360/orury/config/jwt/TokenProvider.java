package com.kernel360.orury.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernel360.orury.domain.user.db.RefreshTokenEntity;
import com.kernel360.orury.domain.user.db.RefreshTokenRepository;
import com.kernel360.orury.domain.user.db.UserEntity;
import com.kernel360.orury.domain.user.db.UserRepository;
import com.kernel360.orury.global.constants.Constant;
import com.kernel360.orury.global.exception.TokenExpiredException;
import com.kernel360.orury.global.exception.TokenNotFoundException;
import com.kernel360.orury.global.message.errors.ErrorMessages;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
	private static final String AUTHORITIES_KEY = "auth";
	private final String secret;
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	private final long accessValidityMs;
	private final long refreshValidityMs;
	private Key key;

	public TokenProvider(
		@Value("${jwt.access-validity}") Long accessValiditySec,
		@Value("${jwt.refresh-validity}") Long refershValiditySec,
		@Value("${jwt.secret}") String secret,
		UserRepository userRepository,
		RefreshTokenRepository tokenRepository
		) {
		this.refreshValidityMs = refershValiditySec * 1000L;
		this.accessValidityMs = accessValiditySec * 1000L;
		this.secret = secret;
		this.userRepository = userRepository;
		this.refreshTokenRepository = tokenRepository;
	}

	// 빈이 생성되고 생성자에서 주입받은 jwt 시크릿 키를 base65 디코드해서 key 변수에 할당
	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}



	public String createAccessToken(Authentication authentication){
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + accessValidityMs);
		UserEntity user = userRepository.findByEmailAddr(authentication.getName()).orElseThrow(
				()-> new RuntimeException(ErrorMessages.THERE_IS_NO_USER.getMessage())
		);

		return Jwts.builder()
				.claim(Constant.USERID.getMessage(), user.getId())
				.setSubject(authentication.getName())
				.claim(AUTHORITIES_KEY, authorities)
				.signWith(key, SignatureAlgorithm.HS512)
				.setExpiration(validity)
				.compact();
	}
	public String createRefreshToken(Authentication authentication){
		long now = (new Date()).getTime();
		Date validity = new Date(now + this.refreshValidityMs);

		//
		var userId = userRepository.findByEmailAddr(authentication.getName()).orElseThrow(
				()-> new RuntimeException(ErrorMessages.THERE_IS_NO_USER.getMessage())
		).getId();

		return Jwts.builder()
				.claim( Constant.USERID.getMessage(), userId)
				.signWith(key, SignatureAlgorithm.HS512)
				.setExpiration(validity)
				.compact();
	}

	// 토큰을 파라미터로 받아서 클레임을 만들고 이를 이용해 유저 객체를 만들고 Authentication 객체 리턴
	public Authentication getAuthentication(String token) throws JsonProcessingException {
//		Claims claims = Jwts
//			.parserBuilder()
//			.setSigningKey(key)
//			.build()
//			.parseClaimsJws(token)
//			.getBody();
		
		String[] chunks = token.split("\\.");
		String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));

		// Jackson ObjectMapper를 사용하여 JSON으로 파싱
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(payload);

		// 필요한 정보 추출
//		String userId = jsonNode.get("userId").asText();
		String sub = jsonNode.get("sub").asText();
		String auth = jsonNode.get("auth").asText();

		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(auth.split(","))
				.map(SimpleGrantedAuthority::new)
				.toList();

		User principal = new User(sub, "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}


	public boolean validateToken(String token ) throws ExpiredJwtException{
		Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		return true;
	}

	public void storeToken(String token){
		Claims claims = Jwts
				.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();

		var userId = Long.parseLong(claims.get("userId").toString());

		var user = userRepository.findById(userId).orElseThrow(
				() -> new RuntimeException(ErrorMessages.THERE_IS_NO_USER.getMessage())
		);

		RefreshTokenEntity existingToken = refreshTokenRepository.findByUserId(userId).orElse(null);
		if(existingToken != null){
			refreshTokenRepository.delete(existingToken);
		}

		var expiredDate = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		var refreshTokenEntity = RefreshTokenEntity.builder()
				.tokenValue(token)
				.user(user)
				.expirationDate(expiredDate)
				.build();
		refreshTokenRepository.save(refreshTokenEntity);
	}

	public boolean validateRefreshToken(String refreshToken) {
		var refreshTokenEntity = refreshTokenRepository.findByTokenValue(refreshToken).orElseThrow(
				() -> new TokenNotFoundException(ErrorMessages.ILLEGAL_REFRESH_JWT.getMessage())
		);
		var expireDate = refreshTokenEntity.getExpirationDate();

		if (LocalDateTime.now().isAfter(expireDate)) {
			throw new TokenExpiredException(ErrorMessages.EXPIRED_REFRESH_JWT.getMessage());
		}

		return validateToken(refreshToken);
	}


}