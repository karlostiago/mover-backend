CREATE TABLE `tb_inspection` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `date` date NOT NULL,
  `inspection_status` enum('APPROVED','REJECTED','UNDER_REVIEW') NOT NULL,
  `mileage` decimal(38,2) NOT NULL,
  `contract_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_inspection_contract_id` (`contract_id`),
  CONSTRAINT `fk_inspection_contract_id` FOREIGN KEY (`contract_id`) REFERENCES `tb_contract` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;