CREATE TABLE `tb_question` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `mandatory` bit(1) NOT NULL,
  `questionnaire_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_question_questionnaire_id` (`questionnaire_id`),
  CONSTRAINT `fk_question_questionnaire_id` FOREIGN KEY (`questionnaire_id`) REFERENCES `tb_questionare` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;