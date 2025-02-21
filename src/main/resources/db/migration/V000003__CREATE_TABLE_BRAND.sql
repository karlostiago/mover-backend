CREATE TABLE `tb_brand` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `symbol_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_brand_symbol_id` (`symbol_id`),
  CONSTRAINT `fk_brand_symbol_id` FOREIGN KEY (`symbol_id`) REFERENCES `tb_symbol` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;