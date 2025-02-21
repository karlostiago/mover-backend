CREATE TABLE `tb_model` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `brand_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_model_brand_id` (`brand_id`),
  CONSTRAINT `fk_model_brand_id` FOREIGN KEY (`brand_id`) REFERENCES `tb_brand` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1634 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;