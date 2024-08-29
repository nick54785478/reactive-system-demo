package com.example.demo.domain.share.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateJWTokenCommand {

	private String username;
	
	private String password;
}
