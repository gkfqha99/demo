package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
	public record RegisterReq(String email, String password, String nickname) {}
	public record LoginReq(String email, String password) {}
	public record TokenRes(String accessToken) {}
	public record MeRes(Long id, String email, String nickname) {}
	/* 비번변경↓ */
	public record ChangePasswordReq(
			@NotBlank String currentPassword,
			@NotBlank @Size(min=6,max=64) String newPassword) {}
}
