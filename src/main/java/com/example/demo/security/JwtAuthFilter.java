package com.example.demo.security;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/* Spring Security + JWT 필터 인증필요한 애들 헤더파싱(각 유저별 요청할때 헤더라는걸 보냄 거기서
각 유저별 토큰꺼내서 검증하는게 헤더파싱 그걸 얘가 여기서 다 처리해줌. */

public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserMapper userMapper;

	public JwtAuthFilter(JwtUtil jwtUtil, UserMapper userMapper) {
		this.jwtUtil = jwtUtil;
		this.userMapper = userMapper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain chain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			// 파싱에 실패하면 GlobalExceptionHandler가 401로 변환
			Jws<Claims> jws = jwtUtil.parse(token);
			Long userId = Long.valueOf(jws.getBody().getSubject());
			User u = userMapper.findById(userId);
			if (u != null) {
				Authentication auth =
						new UsernamePasswordAuthenticationToken(
								userId, // principal: userId만 넣자 (간단)
								null,
								List.of() // 권한은 나중에 ROLE 붙일 때 추가
						);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}

		chain.doFilter(request, response);
	}
}
