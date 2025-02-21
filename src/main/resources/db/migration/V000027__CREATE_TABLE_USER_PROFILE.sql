CREATE TABLE `tb_user_profile` (
  `user_id` bigint NOT NULL,
  `profile_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`profile_id`),
  KEY `fk_profile_profile_id` (`profile_id`),
  CONSTRAINT `fk_profile_profile_id` FOREIGN KEY (`profile_id`) REFERENCES `tb_profile` (`id`),
  CONSTRAINT `fk_profile_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;