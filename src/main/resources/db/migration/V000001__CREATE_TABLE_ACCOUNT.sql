CREATE TABLE `tb_account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `initial_balance` decimal(38,2) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `icon` longtext,
  `caution` bit(1) NOT NULL,
  `active` bit(1) DEFAULT NULL,
  `available_balance` decimal(38,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;