package com.example.demo.common;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/*이제 컨트롤러/서비스에서 throw new IllegalArgumentException("...")
  같은 예외가 나가면 일관된 JSON으로 응답돼.*/

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleBadRequest(IllegalArgumentException e, HttpServletRequest req) {
		return new ErrorResponse(Instant.now(), 400, "Bad Request", e.getMessage(), req.getRequestURI());
	}

	@ExceptionHandler(JwtException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorResponse handleJwt(JwtException e, HttpServletRequest req) {
		return new ErrorResponse(Instant.now(), 401, "Unauthorized", e.getMessage(), req.getRequestURI());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleEtc(Exception e, HttpServletRequest req) {
		return new ErrorResponse(Instant.now(), 500, "Internal Server Error", e.getMessage(), req.getRequestURI());
	}
}
