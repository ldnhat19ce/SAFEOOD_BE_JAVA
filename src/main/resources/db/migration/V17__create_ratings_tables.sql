CREATE TABLE `ratings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP,
  `user_id` bigint ,
  `shop_id` bigint ,
  `ratings` double ,
  `content` varchar(255),
   KEY `fk_ratings_1` (`shop_id`),
   KEY `fk_ratings_2` (`user_id`),
   CONSTRAINT `fk_ratings_1` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`),
   CONSTRAINT `fk_ratings_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  PRIMARY KEY (`id`)
);