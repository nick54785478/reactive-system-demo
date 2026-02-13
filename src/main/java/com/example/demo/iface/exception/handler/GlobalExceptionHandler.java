package com.example.demo.iface.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.infra.exception.exception.ValidationException;
import com.example.demo.infra.exception.response.BaseExceptionResponse;

import reactor.core.publisher.Mono;

/**
 * 全域例外處理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ValidationException.class)
	public Mono<ResponseEntity<BaseExceptionResponse>> handleValidationException(ValidationException ex) {
		String responseBody = ex.getMessage();
		
		switch (ex.getCode()) {
			// 未授權
			case 401: {
				return Mono.just(
						ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseExceptionResponse(401, responseBody)));
			}
			// 沒有訪問資源的權限，拒絕訪問
			case 403: {
				return Mono.just(
						ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BaseExceptionResponse(403, responseBody)));
			}
			// 資料衝突，建立已存在的帳號
			case 409: {
				return Mono.just(
						ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseExceptionResponse(409, responseBody)));
			}
		}
		return Mono.just(
				ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseExceptionResponse(401, responseBody)));
	}

}
