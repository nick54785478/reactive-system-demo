package com.example.demo.iface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserResource {

	private Long id;

	private String name;

	private String email;

	private String address;
	
	private String activeFlag; // 是否有效

	public UpdateUserResource(Long id, String name, String email, String address) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.address = address;
	}
	
	

}
