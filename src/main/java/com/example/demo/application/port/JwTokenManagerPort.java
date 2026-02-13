package com.example.demo.application.port;

import java.util.List;

public interface JwTokenManagerPort {

	/**
	 * 建立 token 參數:
	 * 
	 * @param username 使用者名稱
	 * @param roleList - 角色權限清單
	 * @return JWToken
	 */
	String generateToken(String username, String email, List<String> role);

	/**
	 * 驗證 JWToken 合法性
	 * 
	 * @param token
	 * @return true/false
	 */
	boolean validateToken(String token);

	/**
	 * 取得當前使用者名稱
	 * 
	 * @param token JwToken
	 * @return username
	 */
	String getUsername(String token);

	/**
	 * 取得當前使用者 Email
	 * 
	 * @param token JwToken
	 * @return email
	 */
	String getEmail(String token);

	/**
	 * 取得使用者角色
	 * 
	 * @param token
	 * @return 使用者角色
	 */
	List<String> getRoleList(String token);
}
