CREATE TABLE `tb_inspection_question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `response_enum` enum('NA','NO','YES') NOT NULL,
  `inspection_id` bigint DEFAULT NULL,
  `question_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_inspection_question_inspection_id` (`inspection_id`),
  KEY `fk_inspection_question_question_id` (`question_id`),
  CONSTRAINT `fk_inspection_question_inspection_id` FOREIGN KEY (`inspection_id`) REFERENCES `tb_inspection` (`id`),
  CONSTRAINT `fk_inspection_question_question_id` FOREIGN KEY (`question_id`) REFERENCES `tb_question` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;