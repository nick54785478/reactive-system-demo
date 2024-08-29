package com.example.demo.domain.user.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCommand {

	private Long id;

	private String name;

	private String email; // 信箱

	private String username; // 帳號

	private String password; // 密碼

	private String address;
	
	
}
