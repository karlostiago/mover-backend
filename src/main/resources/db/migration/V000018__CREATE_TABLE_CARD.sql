CREATE TABLE `tb_card` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `card_type` enum('CREDIT','DEBIT','CREDIT_DEBIT') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `closing_day` int DEFAULT NULL,
  `due_date` int DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `limit` decimal(38,2) NOT NULL,
  `name` varchar(255) NOT NULL,
  `account_id` bigint NOT NULL,
  `available_balance` decimal(38,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_card_account_id` (`account_id`),
  CONSTRAINT `fk_card_account_id` FOREIGN KEY (`account_id`) REFERENCES `tb_account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;