package com.example.demo.application.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.example.demo.domain.service.UserService;
import com.example.demo.domain.share.dto.UserInfoData;
import com.example.demo.domain.user.aggregate.UserInfo;
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

	private UserService userService;

	/**
	 * 取得所有使用者清單
	 * 
	 * @return 使用者清單
	 */
	public Flux<UserInfoData> getAllUserList(String username, String tenant, List<String> roleList) {

		Flux<UserInfo> users = roleList.contains("ADMIN") ? userService.getAllUserList()
				: userService.getUserListByTenant(tenant);

		// 沒有角色，那只能看自己的資料
		if (roleList.isEmpty()) {
			users = users.filter(user -> StringUtils.equals(username, user.getUsername()));
		}

		return users.map(user -> BaseDataTransformer.transformData(user, UserInfoData.class));
	}

	/**
	 * 取得該租戶下所有使用者資料 (專給外部 API 使用)
	 * <p>
	 * 只有同租戶的使用者能看到該租戶使用者資料
	 * 
	 * @param tenant 特定租戶
	 * @return 使用者清單
	 */
	public Flux<UserInfoData> getUsersByTenant(String tenant) {
		Flux<UserInfo> users = userService.getUserListByTenant(tenant);
		return users.map(user -> BaseDataTransformer.transformData(user, UserInfoData.class));
	}

	/**
	 * 透過 ID 取得使用者資料
	 * 
	 * @param id 使用者 ID
	 * @return 使用者資訊
	 */
	public Mono<UserInfoData> getUserById(Long id) {
		// 透過 map 將其轉換為 UserInfoData (非領域資料)
		return userService.getUserById(id).map(e -> BaseDataTransformer.transformData(e, UserInfoData.class));
	}

	/**
	 * 透過 Email 取得使用者資料
	 * 
	 * @param tenant
	 * @param email
	 * @return 使用者資訊
	 */
	public Mono<UserInfoData> getUserByEmail(String tenant, String email) {
		// 透過 map 將其轉換為 UserInfoData (非領域資料)
		return userService.getUserByEmail(tenant, email)
				.map(e -> BaseDataTransformer.transformData(e, UserInfoData.class));
	}

}
