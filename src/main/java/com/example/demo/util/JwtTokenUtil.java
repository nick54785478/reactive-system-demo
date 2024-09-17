package com.example.demo.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.config.security.JwtConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

	@Value("${jwt.secret.key}")
	private String scretKey;

	@Value("${jwt.token-expiration-seconds}")
	private long expiration; // 過期時間若是3600L秒，即1個小時

	private static final String ISS = "SYSTEM"; // 簽發者

	/**
	 * 建立 token 參數:
	 * 
	 * @param username 使用者名稱
	 * @param roleList - 角色權限清單
	 * @return JWToken
	 */
	public String generateToken(String username, String email, List<String> role) {
		log.debug("有效時間: " + expiration);
		HashMap<String, Object> map = new HashMap<>();
		map.put(JwtConstants.JWT_CLAIMS_KEY_EMAIL.getValue(), email);
		map.put(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue(), role);
		log.info("map: {}", map);

		Date issDate = new Date(System.currentTimeMillis()); // 簽發日+7天
		log.info("建立Token時的簽發日: {}", issDate);

		Date expDate = new Date(System.currentTimeMillis() + expiration * 1000); // 過期日+7天
		log.info("建立Token欲設置的過期日: {}", expDate);

		return Jwts.builder().claims(map) // 角色
				.issuer(ISS) // 簽發者
				.subject(username) // 使用者名稱
				.issuedAt(issDate) // 簽發日期
				.expiration(expDate) // 過期時間
				.signWith(getSigningKey(), Jwts.SIG.HS256) // 密鑰簽名
				.compact();
	}

	/**
	 * 從 token 中取得使用者名稱
	 * 
	 * @param token
	 * @return 使用者名稱
	 */
	public String getUsername(String token) {
		log.info("getUsername: " + getTokenBody(token).getSubject());
		log.info("TokenBody: " + getTokenBody(token));
		return getTokenBody(token).getSubject();
	}

	/**
	 * 取得使用者角色
	 * 
	 * @param token
	 * @return 使用者角色
	 */
	public List<String> getRoleList(String token) {
		return (List<String>) getTokenBody(token).get(JwtConstants.JWT_CLAIMS_KEY_ROLE.getValue());
	}
	

	/**
	 * 從 token 中取得使用者信箱
	 * 
	 * @param token
	 * @return 使用者信箱
	 */
	public String getEmail(String token) {
		return (String) getTokenBody(token).get(JwtConstants.JWT_CLAIMS_KEY_EMAIL.getValue());
	}

	/**
	 * 取得簽發日
	 * 
	 * @param token
	 * @return 簽發日
	 */
	public Date getIssAt(String token) {
		return getTokenBody(token).getIssuedAt();
	}

	/**
	 * 取得過期日
	 * 
	 * @param token
	 * @return 過期日
	 */
	public Date getExpDate(String token) {
		return getTokenBody(token).getExpiration();
	}

	/**
	 * 是否已過期
	 * 
	 * @param token
	 * @return true/false
	 */
	public boolean isExpiration(String token) {
		return getTokenBody(token).getExpiration().before(new Date());
	}

	/**
	 * 取得 Token 主體
	 * 
	 * @param token
	 * @return Claims
	 */
	public Claims getTokenBody(String token) {
		// 使用 Jwts.parser() 建立 JwtParser 實例
		return Jwts.parser().verifyWith(getSigningKey()) // 用於設定金鑰，該金鑰用於驗證 JWT 令牌的簽名
				.build() // 建置 JwtParser 實例
				.parseSignedClaims(token).getPayload();
	}

	/**
	 * 解析 Token
	 * 
	 * @param token
	 * @return 解析後的結果 Map
	 */
	public Map<String, Object> parseToken(String token) {
		Claims claims = this.getTokenBody(token);
		return claims.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * 取得 SigningKey
	 * 
	 * @return key
	 */
	private SecretKey getSigningKey() {
		log.info("Secret Key: {}", scretKey);
		byte[] keyBytes = Decoders.BASE64.decode(scretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * 驗證 JWToken 合法性
	 * 
	 * @param token
	 * @return true/false
	 */
	public boolean validateToken(String token) {
		Claims claims = this.getTokenBody(token);
		return claims != null;
	}

}
