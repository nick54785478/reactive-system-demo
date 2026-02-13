//package com.example.demo.domain.service;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.example.demo.domain.auth.aggregate.Auth;
//import com.example.demo.domain.role.aggregate.RoleInfo;
//import com.example.demo.infra.jwt.JwTokenGenerator;
//import com.example.demo.infra.repository.AuthRepository;
//import com.example.demo.infra.repository.RoleRepository;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import reactor.core.publisher.Mono;
//
//@Slf4j
//@Service
//@AllArgsConstructor
//public class JWTokenService {
//
//	private AuthRepository authRepository;
//	private RoleRepository roleRepository;
//	private JwTokenGenerator jwTokenGenerator;
//
//	/**
//	 * 建立 JwToken
//	 * 
//	 * @param userId  使用者 ID
//	 * @param command 內含使用者帳號 & 密碼
//	 * @param email   使用者信箱
//	 * @return JWToken
//	 */
//	public Mono<String> generateToken(Long userId, String username, String email) {
//
//		Mono<List<String>> roleList = authRepository.findByUserIdAndActiveFlag(userId, "Y").map(Auth::getRoleId)
//				.collectList() // 將 Flux<Long> 轉為 List<String>
//				.flatMapMany(ids -> roleRepository.findByIdInAndActiveFlag(ids, "Y").map(RoleInfo::getName))
//				.collectList();// 取得角色名稱清單
//
//		log.info("roleList: {}", roleList);
//
//		// 透過 flatMap 處理資料
//		return roleList.flatMap(roles -> {
//			// 透過 Mono.just 轉 String 為 Mono<String>
//			return Mono.just(jwTokenGenerator.generateToken(username, email, roles));
//		});
//	}
//
//}
