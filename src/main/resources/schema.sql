CREATE TABLE IF NOT EXISTS user_info (
    `id` BIGINT(20) AUTO_INCREMENT,
    `name` VARCHAR(100),
    `username` VARCHAR(100),
    `password` VARCHAR(100),
    `address` VARCHAR(255),
    `email` VARCHAR(100),
    `active_flag` CHAR(1),
    `created_by` VARCHAR(20),
    `created_date` DATETIME,
    `last_updated_by` VARCHAR(20),
    `last_updated_date` DATETIME,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS role_info (
    `id` BIGINT(20) AUTO_INCREMENT,
    `name` VARCHAR(100),
    `type` VARCHAR(100),
    `description` VARCHAR(255),
    `active_flag` CHAR(1),
    `created_by` VARCHAR(20),
    `created_date` DATETIME,
    `last_updated_by` VARCHAR(20),
    `last_updated_date` DATETIME,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS auth_info (
    `id` BIGINT(20) AUTO_INCREMENT,
    `user_id` BIGINT(20),
    `role_id` BIGINT(20),
    `active_flag` CHAR(1),    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
