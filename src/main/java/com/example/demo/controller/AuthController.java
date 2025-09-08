package com.example.demo.controller;

import com.example.demo.dto.AuthDtos.*;
import com.example.demo.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	public AuthController(AuthService authService) { this.authService = authService; }

	@PostMapping("/register")
	public MeRes register(@RequestBody RegisterReq req) {
		return authService.register(req);
	}

	@PostMapping("/login")
	public TokenRes login(@RequestBody LoginReq req) {
		return authService.login(req);
	}

	@GetMapping("/me")
	public MeRes me() {
		var auth = org.springframework.security.core.context.SecurityContextHolder
				.getContext().getAuthentication();
		if (auth == null || auth.getPrincipal() == null) {
			throw new IllegalArgumentException("인증이 필요합니다.");
		}
		Long userId = (Long) auth.getPrincipal();
		var u = authService.findById(userId); // 아래에 서비스 메서드 하나 추가
		return new MeRes(u.getId(), u.getEmail(), u.getNickname());
	}

}
