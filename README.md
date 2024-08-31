前言

此專案是基於 SpringFlux 搭配六角形架構實作的 Auth Service 響應式系統範例，主要功能為透過 JWToken 進行使用者權限控制以及對使用者資料進行相關的新、刪、改、查。
架構的設計基於六角形架構，對資料的操作主要是依據 CQRS (命令查詢職責分離) 將 新增修改(Command) 與 查詢(Query) 分開成不同的 Service。
實作此專案的目的主要是想嘗試 Spring WebFlux 及 R2DBC 的相關技術實作，Spring WebFlux 相較於傳統的 MVC 能夠更好地處理大量並發請求而不會阻塞線程，適用於高效能、低延遲的應用場景。


框架及外部依賴:
        SpringBoot 3.3.2
        JDK 17
        MySQL
                     
角色設定
     * ADMIN - 系統管理員 (可進行所有動作)。
     * DATA_OWNER - 資料擁有者 (新增、修改、刪除、讀取所有"使用者"資料權限)。

可依需求自行定義角色。

第一步: 安裝
透過 Docker 或 本地安裝，安裝 MYSQL 資料庫，以下為 yml 檔參考
```
version: "3"
services:
  db:
    image: mysql:8.0
    container_name: local-mysql
    restart: always
    environment:
      TZ: Asia/Taipei
      MYSQL_ROOT_PASSWORD: root 
    command:
      --max_connections=1000
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_unicode_ci
      --default-authentication-plugin=mysql_native_password
    ports:
      - 3306:3306
    volumes:
      - ./data:/var/lib/mysql
      - ./conf:/etc/mysql/conf.d
    networks:
        mysql:
          aliases:
            - mysql
networks:
  mysql:
    name: mysql
    driver: bridge
```
可執行下列指令建立 docker container
```
        docker-compose up -d
```

第二步: 建立表及新增相關資料
```
	CREATE TABLE IF NOT EXISTS user_info (
	    `id` BIGINT(20) AUTO_INCREMENT,
	    `name` VARCHAR(100),
	    `username` VARCHAR(100),
	    `password` VARCHAR(100),
	    `address` VARCHAR(255),
	    `email` VARCHAR(100),
	    `active_flag` CHAR(1),
	    PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
	
	CREATE TABLE IF NOT EXISTS role_info (
	    `id` BIGINT(20) AUTO_INCREMENT,
	    `name` VARCHAR(100),
	    `type` VARCHAR(100),
	    `description` VARCHAR(255),
	    `active_flag` CHAR(1),
	    PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
	
	CREATE TABLE IF NOT EXISTS auth_info (
	    `id` BIGINT(20) AUTO_INCREMENT,
	    `user_id` BIGINT(20),
	    `role_id` BIGINT(20),
	    `active_flag` CHAR(1),    
	    PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

	# system 密碼 system123
	INSERT INTO user_info (name, username, password, address, email, active_flag) VALUES ('System', 'system', '$2a$10$eKT3qdUVQO1mf0.hUZswDuZiO69BKv20OjE3lPITJYqQol4MYAWNm','', 'system@example.com', 'Y');	
	INSERT INTO role_info (name, type, description, active_flag) VALUES ('ADMIN', 'ROOT', '系統管理員', 'Y');
	INSERT INTO role_info (name, type, description, active_flag) VALUES ('DATA_OWNER', 'USER', '新增、修改、刪除、讀取所有使用者、角色資料權限', 'Y');	
	INSERT INTO auth_info (user_id, role_id, active_flag) VALUES (1, 1, 'Y');
````

第三步: 使用Postman 或 WebClient 對其進行測試。
```
{
	"info": {
		"_postman_id": "eaaee464-f2ba-429d-a6ee-c132ef58ac65",
		"name": "reactive-demo",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
		"_exporter_id": "20865420"
	},
	"item": [
		{
			"name": "login",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"username\":\"nick123\",\r\n    \"password\":\"password123\"\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/v1/login",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"v1",
						"login"
					]
				}
			},
			"response": []
		},
		{
			"name": "login admin",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"username\":\"system\",\r\n    \"password\":\"system123\"\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/v1/login",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"v1",
						"login"
					]
				}
			},
			"response": []
		},
		{
			"name": "新增 使用者資料",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"name\": \"Nick\",\r\n\t\"email\": \"nick123@example.com\", // 信箱\r\n\t\"username\":\"nick123\", // 帳號\r\n    \"password\":\"password123\", // 密碼\r\n\t\"address\":\"新北市淡水區\"\t\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/v1/users/register",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"v1",
						"users",
						"register"
					]
				}
			},
			"response": []
		},
		{
			"name": "更新 使用者資料",
			"request": {
				"auth": {
					"type": "bearer",
					"bearer": [
						{
							"key": "token",
							"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJEQVRBX09XTkVSIl0sImlzcyI6IlNZU1RFTSIsInN1YiI6Im5pY2sxMjMiLCJpYXQiOjE3MjUxMzUxNDcsImV4cCI6MTcyNTEzODc0N30.Pdw3jaEMrLaRm1V_pfLwHA1u_-qA6S1Bn0tGCVbx9r4",
							"type": "string"
						}
					]
				},
				"method": "PUT",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n    \"id\":2,\r\n    \"name\": \"Nick111\",\r\n\t\"email\": \"nick123@example.com\", // 信箱\r\n\t\"username\":\"nick123\", // 帳號\r\n    \"password\":\"password123\", // 密碼\r\n\t\"address\":\"新北市淡水區\"\t\r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/v1/users/register",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"v1",
						"users",
						"register"
					]
				}
			},
			"response": []
		},
		{
			"name": "賦予使用者權限",
			"request": {
				"auth": {
					"type": "bearer",
					"bearer": [
						{
							"key": "token",
							"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJTWVNURU0iLCJzdWIiOiJzeXN0ZW0iLCJpYXQiOjE3MjQ5Mzc3MDQsImV4cCI6MTcyNDk0MTMwNH0.iXHaQmE_RNfkv264rBxBYOExW9_SdQX-qVw85fTjVoI",
							"type": "string"
						}
					]
				},
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\r\n   \t\"username\":\"nick123\", // 帳號\r\n    \"roleList\": [\r\n        2\r\n    ] \r\n}",
					"options": {
						"raw": {
							"language": "json"
						}
					}
				},
				"url": {
					"raw": "http://localhost:8080/api/v1/auth",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"v1",
						"auth"
					]
				}
			},
			"response": []
		},
		{
			"name": "透過 userId 取得 User 資料",
			"request": {
				"auth": {
					"type": "bearer",
					"bearer": [
						{
							"key": "token",
							"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W10sImlzcyI6IlNZU1RFTSIsInN1YiI6Im1heDEyMyIsImlhdCI6MTcyNDg2NDI5MSwiZXhwIjoxNzI0ODY3ODkxfQ.OWy6pGZSUZZva-Jl0Eyk0JexRdEXaoIo39JMMHUHZwo",
							"type": "string"
						}
					]
				},
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/v1/users/3",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"v1",
						"users",
						"3"
					]
				}
			},
			"response": []
		},
		{
			"name": "透過 RoleId 取得 Role 資料",
			"request": {
				"auth": {
					"type": "bearer",
					"bearer": [
						{
							"key": "token",
							"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJEQVRBX09XTkVSIl0sImlzcyI6IlNZU1RFTSIsInN1YiI6Im5pY2sxMjMiLCJpYXQiOjE3MjQ4Njc2NDIsImV4cCI6MTcyNDg3MTI0Mn0.YAsUmX5-B-e1MHOMBN4t3qt50d_R12Lnl3zIzkkvxJI",
							"type": "string"
						}
					]
				},
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/v1/roles/1",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"v1",
						"roles",
						"1"
					]
				}
			},
			"response": []
		},
		{
			"name": "取得 User 資料清單",
			"request": {
				"auth": {
					"type": "bearer",
					"bearer": [
						{
							"key": "token",
							"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJTWVNURU0iLCJzdWIiOiJzeXN0ZW0iLCJpYXQiOjE3MjQ5Mzc3MDQsImV4cCI6MTcyNDk0MTMwNH0.iXHaQmE_RNfkv264rBxBYOExW9_SdQX-qVw85fTjVoI",
							"type": "string"
						}
					]
				},
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/v1/users",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"v1",
						"users"
					]
				}
			},
			"response": []
		},
		{
			"name": "取得 Role 資料清單",
			"request": {
				"auth": {
					"type": "bearer",
					"bearer": [
						{
							"key": "token",
							"value": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJBRE1JTiJdLCJpc3MiOiJTWVNURU0iLCJzdWIiOiJzeXN0ZW0iLCJpYXQiOjE3MjQ4NjgwMTgsImV4cCI6MTcyNDg3MTYxOH0.qQ_jspfRkvBCChT7HJkoeTlQjp5K89o9Ap5PjpALrGE",
							"type": "string"
						}
					]
				},
				"method": "GET",
				"header": [],
				"url": {
					"raw": "http://localhost:8080/api/v1/roles",
					"protocol": "http",
					"host": [
						"localhost"
					],
					"port": "8080",
					"path": [
						"api",
						"v1",
						"roles"
					]
				}
			},
			"response": []
		}
	]
}
```

