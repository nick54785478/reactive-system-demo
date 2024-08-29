package com.example.demo.domain.role.aggregate;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.mapping.Table;

import com.example.demo.domain.role.aggregate.vo.RoleActive;
import com.example.demo.domain.role.command.CreateRoleCommand;
import com.example.demo.domain.role.command.UpdateRoleCommand;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 角色表
 */
@Getter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_info")
@EntityListeners(AuditingEntityListener.class)
public class RoleInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String type; // 權限種類

	private String description; // 敘述

	private String activeFlag = "Y"; // 是否有效
	

	public void create(CreateRoleCommand command) {
		this.name = command.getName();
		this.description = command.getDescription();
		this.type = command.getType();
		this.activeFlag = RoleActive.ACTIVE.getValue();
	}

	public void update(UpdateRoleCommand command) {
		this.name = command.getName();
		this.type = command.getType();
		this.description = command.getDescription();
		this.activeFlag = RoleActive.ACTIVE.getValue();
	}

	/**
	 * 刪除使用者資料 (ActiveFlag = "N")
	 * 
	 */
	public void delete() {
		this.activeFlag = RoleActive.INACTIVE.getValue();
	}

}
