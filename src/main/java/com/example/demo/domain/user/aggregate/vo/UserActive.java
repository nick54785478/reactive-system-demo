package com.example.demo.domain.user.aggregate.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UserActive {
	ACTIVE("Y"), INACTIVE("N");

	@Getter
	private String value;
}
