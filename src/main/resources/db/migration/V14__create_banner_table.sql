CREATE TABLE `shop_image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image` varchar(255),
  `shop_id` bigint,
  KEY `fk_banner` (`shop_id`),
  CONSTRAINT `fk_banner` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`),
  PRIMARY KEY (`id`)
);