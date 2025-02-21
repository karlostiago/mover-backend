CREATE TABLE `tb_fipe` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `brand` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL,
  `fuel` varchar(255) NOT NULL,
  `hash` varchar(255) NOT NULL,
  `model` varchar(255) NOT NULL,
  `model_year` int NOT NULL,
  `price` decimal(38,2) NOT NULL,
  `reference_month` varchar(255) NOT NULL,
  `reference_year` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;