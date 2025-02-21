CREATE TABLE `tb_photo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `image` longtext NOT NULL,
  `questionnaire_id` bigint DEFAULT NULL,
  `mandatory` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_photo_questionnaire_id` (`questionnaire_id`),
  CONSTRAINT `fk_photo_questionnaire_id` FOREIGN KEY (`questionnaire_id`) REFERENCES `tb_questionare` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=249 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;