package com.example.demo.domain.role.aggregate.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RoleActive {
	
	ACTIVE("Y"), INACTIVE("N");

	@Getter
	private String value;
}

