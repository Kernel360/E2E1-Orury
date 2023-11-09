package com.kernel360.orury.config.jwt;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
	private static final String AUTHORITIES_KEY = "auth";
	private final String secret;
	private final long ACCESS_VALIDITY_MS = 30*1000L;
	private final long REFRESH_VALIDITY_MS =  7*24*60*60*1000L;
	private Key key;

	public TokenProvider(
		@Value("${jwt.secret}") String secret) {
		this.secret = secret;
	}

	// 빈이 생성되고 생성자에서 주입받은 jwt 시크릿 키를 base65 디코드해서 key 변수에 할당
	@Override
	public void afterPropertiesSet() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	// Authentication을 파라미터로 받아서 권한들을 가져온다, yml 파일에 설정한 만료시간을 설정하고 토큰을 생성한다
	public String createToken(Authentication authentication, Long tokenValidity) {
		String authorities = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + tokenValidity);

		return Jwts.builder()
			.setSubject(authentication.getName())
			.claim(AUTHORITIES_KEY, authorities)
			.signWith(key, SignatureAlgorithm.HS512)
			.setExpiration(validity)
			.compact();
	}
	public String createAccessToken(Authentication authentication){
		return createToken(authentication, this.ACCESS_VALIDITY_MS);
	}
	public String createRefreshToken(Authentication authentication){
		return createToken(authentication, this.REFRESH_VALIDITY_MS);
	}

	// 토큰을 파라미터로 받아서 클레임을 만들고 이를 이용해 유저 객체를 만들고 Authentication 객체 리턴
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts
			.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public boolean validateToken(String token) throws ExpiredJwtException{
		Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		return true;
	}
}