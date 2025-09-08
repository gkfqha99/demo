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
	public MeRes me(@RequestHeader("Authorization") String authHeader) {
		return authService.me(authHeader);
	}
}
