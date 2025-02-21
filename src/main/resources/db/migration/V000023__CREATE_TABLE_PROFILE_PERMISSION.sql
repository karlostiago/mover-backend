CREATE TABLE `tb_profile_permission` (
  `profile_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  PRIMARY KEY (`profile_id`,`permission_id`),
  KEY `FK_profile_permission_permission_id` (`permission_id`),
  CONSTRAINT `fk_profile_permission_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `tb_permission` (`id`),
  CONSTRAINT `fk_profile_permission_profile_id` FOREIGN KEY (`profile_id`) REFERENCES `tb_profile` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;