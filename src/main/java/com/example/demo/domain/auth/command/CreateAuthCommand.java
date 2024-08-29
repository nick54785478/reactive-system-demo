package com.example.demo.domain.auth.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAuthCommand {

	private String username; // 使用者帳號

	private List<Long> roleList; // 角色 ID 列表
}
