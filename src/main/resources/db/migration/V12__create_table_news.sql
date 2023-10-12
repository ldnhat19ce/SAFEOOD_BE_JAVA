CREATE TABLE `news` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `title` text ,
  `location` varchar(255) ,
  `content` varchar(255) ,
  `image` varchar(255),
  `sub_title` varchar(255),
  `address` varchar(255),
  PRIMARY KEY (`id`)
);