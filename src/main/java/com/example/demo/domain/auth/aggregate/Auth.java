package com.example.demo.domain.auth.aggregate;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 權限表
 */
@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_info")
@EntityListeners(AuditingEntityListener.class)
public class Auth {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId; // 使用者 ID

	private Long roleId; // 角色 ID

	private String activeFlag = "Y"; // 是否有效
	
	
	
	public void create(Long userId, Long roleId) {
		this.userId = userId;
		this.roleId = roleId;
		this.activeFlag = "Y";
	}

}
