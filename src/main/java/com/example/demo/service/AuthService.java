package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.User;
import com.example.demo.dto.AuthDtos.*;
import com.example.demo.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
	private final UserMapper userMapper;
	private final JwtUtil jwtUtil;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	public AuthService(UserMapper userMapper, JwtUtil jwtUtil) {
		this.userMapper = userMapper;
		this.jwtUtil = jwtUtil;
	}

	/**
	 * 회원가입
	 * - 이메일 중복 체크
	 * - 비밀번호 해싱 후 저장
	 * - 예외 발생 시 ROLLBACK
	 */
	@Transactional
	public MeRes register(RegisterReq req) {
		if (userMapper.findByEmail(req.email()) != null) {
			throw new IllegalArgumentException("이미 가입된 이메일입니다.");
		}

		User u = new User();
		u.setEmail(req.email());
		u.setPassword(encoder.encode(req.password())); // 비밀번호 해시 저장
		u.setNickname(req.nickname());

		userMapper.insert(u);

		return new MeRes(u.getId(), u.getEmail(), u.getNickname());
	}

	/**
	 * 로그인
	 * - 이메일/비밀번호 확인
	 * - JWT 발급
	 */
	public TokenRes login(LoginReq req) {
		User u = userMapper.findByEmail(req.email());
		if (u == null || !encoder.matches(req.password(), u.getPassword())) {
			throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
		}
		String token = jwtUtil.create(u.getId(), u.getEmail());
		return new TokenRes(token);
	}

	/**
	 * 내 정보 조회 (헤더 직접 파싱 방식)
	 */
	public MeRes me(String bearerToken) {
		String token = bearerToken.replace("Bearer ", "");
		var claims = jwtUtil.parse(token).getBody();
		Long id = Long.valueOf(claims.getSubject());
		User u = userMapper.findById(id);
		return new MeRes(u.getId(), u.getEmail(), u.getNickname());
	}

	/**
	 * 사용자 ID로 직접 조회
	 * - SecurityContext에서 userId 꺼낸 후 사용
	 */
	public User findById(Long id) {
		return userMapper.findById(id);
	}
}
