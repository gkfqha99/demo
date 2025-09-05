package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.nio.charset.StandardCharsets; // ★ 추가
import java.util.Date;

@Component
public class JwtUtil {
	private final Key key;
	private final long validityMs;

	public JwtUtil(@Value("${jwt.secret}") String secret,
				   @Value("${jwt.access-token-validity-seconds}") long validitySec) {
		// 기본 charset 말고 UTF-8 고정
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.validityMs = validitySec * 1000;
	}

	public String create(Long userId, String email) {
		Date now = new Date();
		return Jwts.builder()
				.setSubject(String.valueOf(userId))
				.claim("email", email)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + validityMs))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public Jws<Claims> parse(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	}
}
