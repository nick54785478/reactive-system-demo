package com.example.demo.infra.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.user.aggregate.UserInfo;

import reactor.core.publisher.Mono;;

@Repository
public interface UserRepository extends R2dbcRepository<UserInfo, Long> {

	Mono<UserInfo> findByUsername(String username);

	Mono<UserInfo> findByEmail(String email);

	@Modifying
	@Query(value = "UPDATE user_info SET active_flag = 'N' WHERE id = :id")
	Mono<Integer> deleteByUserId(Long id);

	@Modifying
	@Query(value = "UPDATE user_info SET name = :name, address = :address, email = :email WHERE id = :id")
	Mono<Integer> updateUserInfoById(String name, String address, String email, Long id);
}
