CREATE TABLE `tb_inspection_photo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `inspection_status` enum('APPROVED','REJECTED','UNDER_REVIEW') NOT NULL,
  `inspection_id` bigint DEFAULT NULL,
  `photo_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_inspection_photo_inspection_id` (`inspection_id`),
  KEY `fk_inspection_photo_photo_id` (`photo_id`),
  CONSTRAINT `fk_inspection_photo_inspection_id` FOREIGN KEY (`inspection_id`) REFERENCES `tb_inspection` (`id`),
  CONSTRAINT `fk_inspection_photo_photo_id` FOREIGN KEY (`photo_id`) REFERENCES `tb_photo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;