package com.example.demo.domain.user.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserCommand {
	
	private Long id;

	private String name;

	private String email;

	private String address;
	
	private String activeFlag;
	
}
