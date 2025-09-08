package com.example.demo.common;

import java.time.Instant;

/*공통 에러 응답 DTO*/
public record ErrorResponse(
		Instant timestamp,
		int status,
		String error,
		String message,
		String path
) {}
