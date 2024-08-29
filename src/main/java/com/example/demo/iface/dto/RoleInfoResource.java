package com.example.demo.iface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleInfoResource {

	private Long id;

	private String name;
	
	private String type;

	private String description; // 敘述

	private String activeFlag; // 是否有效

}
