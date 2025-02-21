CREATE TABLE `tb_parameter` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `key` varchar(255) NOT NULL,
  `type_value` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TRIGGER BEFORE_DELETE_PARAMETER
BEFORE DELETE ON tb_parameter
FOR EACH ROW
BEGIN
    IF OLD.key IN ('DESVALORIZACAO_FIPE', 'DESVALORIZACAO_FIPE_LEILAO') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Essa é uma parametrização essencial para o sistema e não pode ser removida.';
    END IF;
END

CREATE TRIGGER BEFORE_UPDATE_PARAMETER
BEFORE UPDATE ON tb_parameter
FOR EACH ROW
BEGIN
    IF OLD.KEY IN ('DESVALORIZACAO_FIPE', 'DESVALORIZACAO_FIPE_LEILAO') AND NEW.KEY != OLD.KEY THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Essa é uma parametrização essencial para o sistema e não pode ser atualizada.';
    END IF;
END

INSERT INTO tb_parameter (id, active, `key`, type_value, value) VALUES(15, 1, 'DESVALORIZACAO_FIPE', 'PERCENT', '15');
INSERT INTO tb_parameter (id, active, `key`, type_value, value) VALUES(16, 1, 'DESVALORIZACAO_FIPE_LEILAO', 'PERCENT', '30');