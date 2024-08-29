package com.example.demo.domain.share;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoData {

	private Long id;

	private String name;
	
	private String username; // 帳號

	private String email;

	private String address;
	
	private String activeFlag;

}
