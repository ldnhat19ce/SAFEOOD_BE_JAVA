CREATE TABLE `review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP,
  `content` text,
  `title` varchar(255),
  `shop_id` bigint ,
  `user_id` bigint ,
   KEY `fk_review_1` (`shop_id`),
   KEY `fk_review_2` (`user_id`),
   CONSTRAINT `fk_review_1` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`),
   CONSTRAINT `fk_review_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  PRIMARY KEY (`id`)
);


CREATE TABLE `review_images` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP,
  `images_url` varchar(255),
  `review_id` bigint,
   KEY `fk_review_images_1` (`review_id`),
   CONSTRAINT `fk_review_images_1` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`),
  PRIMARY KEY (`id`)
);

CREATE TABLE `reply` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP,
  `content` text,
  `review_id` bigint,
  `user_id` bigint,
   KEY `fk_reply_1` (`review_id`),
   KEY `fk_reply_2` (`user_id`),
   CONSTRAINT `fk_reply_1` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`),
   CONSTRAINT `fk_reply_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  PRIMARY KEY (`id`)
);

CREATE TABLE `re_reply` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP,
  `content` text,
  `reply_id` bigint,
  `user_id` bigint,
   KEY `fk_re_reply_1` (`reply_id`),
   KEY `fk_re_reply_2` (`user_id`),
   CONSTRAINT `fk_re_reply_1` FOREIGN KEY (`reply_id`) REFERENCES `reply` (`id`),
   CONSTRAINT `fk_re_reply_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  PRIMARY KEY (`id`)
);



