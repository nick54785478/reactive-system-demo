package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.response.BaseExceptionResponse;

import reactor.core.publisher.Mono;

/**
 * 全域例外處理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ValidationException.class)
	public Mono<ResponseEntity<BaseExceptionResponse>> handleValidationException(ValidationException ex) {
		String responseBody = ex.getMessage();
		return Mono.just(
				ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseExceptionResponse(401, responseBody)));
	}

}
