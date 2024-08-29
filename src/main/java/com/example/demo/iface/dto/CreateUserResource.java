package com.example.demo.iface.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResource {

	private Long id;

	private String name;

	private String email; // 信箱

	private String username; // 帳號

	private String password; // 密碼

	private String address;
}
