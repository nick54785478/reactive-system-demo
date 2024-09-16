<h3>背景</h3>
<hr />

&emsp;&emsp;此專案是基於 SpringFlux 搭配六角形架構實作的 Auth Service 響應式系統範例，主要功能為透過 JWToken 進行使用者權限控制以及對使用者資料進行維護。
架構的設計基於六角形架構，對資料的操作依據 CQRS (命令查詢職責分離) 將增修(Command) 與 查詢(Query) 分開成不同的 Service，以利後續維護。

實作此範例的目的主要是想嘗試 Spring WebFlux 及 R2DBC 相關技術框架的實作。

&emsp;&emsp;Spring WebFlux 框架是一種以資料流和變化為導向的程式設計方式，意味著可以在程式語言中很方便地表達靜態或動態的資料流，而相關的計算模型會自動將變化的值透過資料流傳播。
相較於傳統的 MVC 架構能夠更好地處理大量並發請求而不會阻塞線程，適用於高效能、低延遲的應用場景，此特性非常契合微服務的建置。

&emsp;&emsp;其運作方式是透過由 Subscriber 向 Publisher 進行訂閱 (Subscription)來取得資料流，例如: 當有一個帶有一定延遲的才能夠返回的 io 操作時，
不會發生阻塞，而是立刻返回一個流，並且透過訂閱這個流，當這個流上產生了返回數據，可以立刻得到通知並調用回調函數處理數據。

在此框架中有兩個 Publisher 

> * Mono：一個只會發佈單一項（或無項）資料的 Publisher。
> * Flux：一個發布多項資料（零個或多個）的 Publisher。

我們可以透過請求去訂閱他們，並取得相關資料。


<br/>

<h3>框架及外部依賴</h3>

>* Java
>* SpringBoot 3.3.2
>* JDK 17
>* MySQL

<br/>
		     
<h3>角色設計</h3>

> * ADMIN - 系統管理員 (可進行所有動作)。
> * DATA_OWNER - 資料擁有者 (新增、修改、刪除、讀取所有"使用者"資料權限)。

可依需求自行定義角色。


<br/>

<h3>第一步: 安裝</h3>
	透過 Docker 或 本地安裝，安裝 MYSQL 資料庫，以下為 yml 檔參考:

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
<br/>


**可執行下列指令建立 docker container**

```
        docker-compose up -d
```




<h3>第二步: 建立表及新增相關資料</h3>
相關表與資料如下:

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
```

<br />


<h3>第三步: 使用Postman 或 WebClient 對其進行測試</h3>

**註. 請先執行第二步，新增 Admin 角色資料，之後註冊新帳號，將DATA_OWNER 權限賦予給該帳號，開始執行後續。**

> * Postman 作法:
根據 iface.handler 中的 URL 去建立 Request (有些要記得設置 Token，透過 LoginHandler 內 /login 取得 JWToken )。
> * WebClient 作法:
可參考 ReactiveSystemDemoApplicationTests，裡面有示範。


