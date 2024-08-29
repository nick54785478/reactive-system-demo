package com.example.demo.domain.role.aggregate.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AuthType {
	
	ROOT("ROOT"), USER("USER"), ROLE("ROLE");

	@Getter
	private String value;
}

