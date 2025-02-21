CREATE TABLE `tb_user_permission` (
  `user_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`permission_id`),
  KEY `fk_user_permission_permission_id` (`permission_id`),
  CONSTRAINT `fk_user_permission_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `tb_permission` (`id`),
  CONSTRAINT `fk_user_permission_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;