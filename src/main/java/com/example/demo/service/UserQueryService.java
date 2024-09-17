package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.domain.service.UserService;
import com.example.demo.domain.share.UserInfoData;
import com.example.demo.util.BaseDataTransformer;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Application Service
 */
@Service
@AllArgsConstructor
public class UserQueryService {

	UserService userService;

	/**
	 * 取得使用者清單
	 * 
	 * @return 使用者清單
	 */
	public Flux<UserInfoData> getUserList() {
		return userService.getUserList() // 取得 Flux<UserInfo>
				// 透過 map 將其轉換為 UserInfoData (非領域資料)
				.map(e -> BaseDataTransformer.transformData(e, UserInfoData.class));
	}

	/**
	 * 透過 ID 取得使用者資料
	 * 
	 * @param id
	 * @return 使用者資訊
	 */
	public Mono<UserInfoData> getUserById(Long id) {
		// 透過 map 將其轉換為 UserInfoData (非領域資料)
		return userService.getUserById(id).map(e -> BaseDataTransformer.transformData(e, UserInfoData.class));
	}
	
	/**
	 * 透過 Email 取得使用者資料
	 * 
	 * @param email
	 * @return 使用者資訊
	 */
	public Mono<UserInfoData> getUserByEmail(String email) {
		// 透過 map 將其轉換為 UserInfoData (非領域資料)
		return userService.getUserByEmail(email).map(e -> BaseDataTransformer.transformData(e, UserInfoData.class));
	}

}
