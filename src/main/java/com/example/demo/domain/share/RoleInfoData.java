package com.example.demo.domain.share;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoData {

	private Long id;

	private String name;
	
	private String type;

	private String description; // 敘述

	private String activeFlag; // 是否有效

}
