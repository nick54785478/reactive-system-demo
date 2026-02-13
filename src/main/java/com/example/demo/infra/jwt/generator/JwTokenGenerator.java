package com.example.demo.infra.jwt.generator;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.infra.jwt.shared.constant.JwtConstants;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwTokenGenerator {

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
		log.debug("有效時間: {}", expiration);
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
	 * 取得 SigningKey
	 * 
	 * @return key
	 */
	private SecretKey getSigningKey() {
		log.info("Secret Key: {}", scretKey);
		byte[] keyBytes = Decoders.BASE64.decode(scretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
