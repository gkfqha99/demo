package com.example.demo.dto;

public class AuthDtos {
	public record RegisterReq(String email, String password, String nickname) {}
	public record LoginReq(String email, String password) {}
	public record TokenRes(String accessToken) {}
	public record MeRes(Long id, String email, String nickname) {}
}
