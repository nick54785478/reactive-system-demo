package com.example.demo.domain.user.aggregate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.relational.core.mapping.Table;

import com.example.demo.domain.user.aggregate.vo.UserActive;
import com.example.demo.domain.user.command.CreateUserCommand;
import com.example.demo.domain.user.command.UpdateUserCommand;
import com.example.demo.util.PasswordUtil;

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
@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
@EntityListeners(AuditingEntityListener.class)
public class UserInfo {

	/*
	 * 註. 資料庫欄位 與 Entity 的值無法以 @Column 去 Mapping ， 需依照 columnName (JAVA 駝峰) 與資料庫
	 * column_name( _ 隔開) 對照。
	 */

	// 是引用 import org.springframework.data.annotation.Id; 而非 jakarta.persistence.Id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email; // 信箱

	private String username; // 帳號

	private String password; // 密碼

	private String address;

	private String activeFlag = "Y"; // 是否有效

	/**
	 * Constructor
	 */
	public UserInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * 新增使用者資料
	 * 
	 * @param command
	 * @param passwordEncoder 密碼加密器
	 */
	public void create(CreateUserCommand command) {
		this.name = command.getName();
		this.username = command.getUsername();
		this.password = PasswordUtil.encode(command.getPassword());
		this.email = command.getEmail();
		this.address = command.getAddress();
		this.activeFlag = UserActive.ACTIVE.getValue();
	}

	/**
	 * 更新使用者資料
	 * 
	 * @param command
	 */
	public void update(UpdateUserCommand command) {
		this.name = (StringUtils.isNotBlank(command.getName())) ? command.getName() : this.name;
		this.email = (StringUtils.isNotBlank(command.getEmail())) ? command.getEmail() : this.email;
		this.address = (StringUtils.isNotBlank(command.getAddress())) ? command.getAddress() : this.address;
		this.activeFlag = command.getActiveFlag();

	}

	/**
	 * 刪除使用者資料 (ActiveFlag = "N")
	 * 
	 */
	public void delete() {
		this.activeFlag = UserActive.INACTIVE.getValue();
	}

}
