package com.example.demo.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.demo.config.security.filter.AuthorityHandlerFilterFunction;
import com.example.demo.config.security.filter.JwtHandlerFilterFunction;
import com.example.demo.iface.handler.AuthHandler;
import com.example.demo.iface.handler.LoginHandler;
import com.example.demo.iface.handler.RoleHandler;
import com.example.demo.iface.handler.UserHandler;
import com.example.demo.util.JwtTokenUtil;

/**
 * Functional Endpoint 配置
 */
@Configuration(proxyBeanMethods = false)
public class RouteConfig {

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	/**
	 * 用於定義路由函數及構建服務器響應 RouterFunction<ServerResponse> 響應 登入請求
	 */
	@Bean
	public RouterFunction<ServerResponse> routesForlogin(LoginHandler handler) {
		return RouterFunctions.route(RequestPredicates.POST("/api/v1/login"), handler::getJWToken);

	}

	/**
	 * 用於定義路由函數及構建服務器響應 RouterFunction<ServerResponse> 響應 User 資料的 新、刪、改、查
	 */
	@Bean
	public RouterFunction<ServerResponse> routes(UserHandler handler) {
		return RouterFunctions.route(RequestPredicates.GET("/api/v1/users"), handler::getUserList)
				.andRoute(
						RequestPredicates.GET("/api/v1/users/queryByEmail")
								.and(RequestPredicates.queryParam("email", email -> StringUtils.isNotBlank(email))),
						handler::getUserByEmail)  // 有 RequestParam 的參數須注意，不能放在 /{id} 後面，不然會選擇該路徑
				.andRoute(RequestPredicates.GET("/api/v1/users/{id}"), handler::getUser)
				.andRoute(RequestPredicates.POST("/api/v1/users/register"), handler::createUser)
				.andRoute(RequestPredicates.PUT("/api/v1/users/{id}"), handler::updateUser)
				.andRoute(RequestPredicates.DELETE("/api/v1/users/{id}"), handler::deleteUser)
				.filter(new AuthorityHandlerFilterFunction()) // Authority Filter
				.filter(new JwtHandlerFilterFunction(jwtTokenUtil)); // JWToken Filter
		// Filter 順序由下往上，越下面的會先進入
	}

	/**
	 * 用於定義路由函數及構建服務器響應 RouterFunction<ServerResponse> 響應 Role 資料的 新、刪、改、查
	 */
	@Bean
	public RouterFunction<ServerResponse> routesForRoles(RoleHandler handler) {
		return RouterFunctions.route(RequestPredicates.GET("/api/v1/roles/{id}"), handler::getRole)
				.andRoute(RequestPredicates.GET("/api/v1/roles"), handler::getRoleList)
				.andRoute(RequestPredicates.POST("/api/v1/roles"), handler::createRole)
				.andRoute(RequestPredicates.PUT("/api/v1/roles/{id}"), handler::updateRole)
				.andRoute(RequestPredicates.DELETE("/api/v1/roles/{id}"), handler::deleteRole)
				.filter(new AuthorityHandlerFilterFunction()) // Authority Filter
				.filter(new JwtHandlerFilterFunction(jwtTokenUtil)); // JWToken Filter;
	}

	/**
	 * 用於定義路由函數及構建服務器響應 RouterFunction<ServerResponse> 響應 Auth 資料的 新、刪、改、查
	 */
	@Bean
	public RouterFunction<ServerResponse> routesForAuth(AuthHandler handler) {
		return RouterFunctions.route(RequestPredicates.POST("/api/v1/auth"), handler::grantAuth)
				.filter(new JwtHandlerFilterFunction(jwtTokenUtil));
	}

}
