package com.example.demo.infra.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.auth.aggregate.Auth;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AuthRepository extends R2dbcRepository<Auth, Long> {

	Flux<Auth> findByUserIdAndActiveFlag(Long userId, String activeFlag);

	Mono<Auth> deleteByUserId(Long userId);
	
	@Modifying
	@Query(value = "DELETE FROM auth_info WHERE user_id = :userId AND role_id <> 1")
	Mono<Long> deleteByUserIdAndReserveAdmin(@Param("userId") Long userId);

}
