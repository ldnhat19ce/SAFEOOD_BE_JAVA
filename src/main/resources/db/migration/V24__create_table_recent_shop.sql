CREATE TABLE `recent_shop` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP,
  `shop_id` bigint ,
  `user_id` bigint ,
   KEY `fk_recent_1` (`shop_id`),
   KEY `fk_recent_2` (`user_id`),
   CONSTRAINT `fk_recent_1` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`),
   CONSTRAINT `fk_recent_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  PRIMARY KEY (`id`)
);