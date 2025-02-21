CREATE TABLE `tb_vehicle` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `acquisition_date` date DEFAULT NULL,
  `auction` bit(1) DEFAULT NULL,
  `availability_date` date DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL,
  `fipe_depreciation` decimal(38,2) DEFAULT NULL,
  `fipe_value_at_acquisition` decimal(38,2) DEFAULT NULL,
  `license_plate` varchar(255) NOT NULL,
  `mileage_at_acquisition` decimal(38,2) DEFAULT NULL,
  `model_year` int DEFAULT NULL,
  `renavam` varchar(255) DEFAULT NULL,
  `situation` varchar(255) NOT NULL,
  `year_manufacture` int DEFAULT NULL,
  `brand_id` bigint NOT NULL,
  `model_id` bigint NOT NULL,
  `value_acquisition` decimal(38,2) NOT NULL,
  `fuel_type` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_vehicle_brand_id` (`brand_id`),
  KEY `fk_vehicle_model_id` (`model_id`),
  CONSTRAINT `fk_vehicle_brand_id` FOREIGN KEY (`brand_id`) REFERENCES `tb_brand` (`id`),
  CONSTRAINT `fk_vehicle_model_id` FOREIGN KEY (`model_id`) REFERENCES `tb_model` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;