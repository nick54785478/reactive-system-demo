package com.example.demo.infra.repository;

import java.util.List;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.role.aggregate.RoleInfo;

import reactor.core.publisher.Flux;

@Repository
public interface RoleRepository extends R2dbcRepository<RoleInfo, Long> {

	Flux<RoleInfo> findByIdInAndActiveFlag(List<Long> ids, String activeFlag);

}
