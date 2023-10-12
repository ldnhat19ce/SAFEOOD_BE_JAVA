
CREATE TABLE `shop_fauvourite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `shop_id` bigint DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_fauvourite_1` (`shop_id`),
  KEY `fk_fauvourite_2` (`user_id`),
  CONSTRAINT `fk_fauvourite_1` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`),
  CONSTRAINT `fk_fauvourite_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `delete_flag` bit DEFAULT FALSE,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
);

CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` decimal(10,0) NOT NULL,
  `status` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `category_id` bigint DEFAULT NULL,
  `shop_id` bigint DEFAULT NULL,
  `delete_flag` bit DEFAULT FALSE,
  PRIMARY KEY (`id`),
  KEY `pk_product_1` (`category_id`),
  KEY `pk_product_2` (`shop_id`),
  CONSTRAINT `pk_product_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `pk_product_2` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`)
);

CREATE TABLE `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint DEFAULT NULL,
  `shop_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `amount` bigint DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_product_cart` (`product_id`),
  KEY `fk_shop_cart` (`shop_id`),
  KEY `fk_user_cart` (`user_id`),
  CONSTRAINT `fk_product_cart` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_shop_cart` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`),
  CONSTRAINT `fk_user_cart` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `addresses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(50) DEFAULT NULL,
  `district` varchar(50) DEFAULT NULL,
  `town` varchar(50) DEFAULT NULL,
  `street` varchar(50) DEFAULT NULL,
  `shop_id` bigint DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `x` int DEFAULT NULL,
  `y` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pk_addresses` (`shop_id`),
  CONSTRAINT `pk_addresses` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`)
);

CREATE TABLE `voucher` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `delete_flag` bit DEFAULT FALSE,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ended_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `shop_id` bigint,
  `user_type` varchar(50),
  `value_discount` double,
  `voucher_type` varchar(50),
  `value_need` double,
  `limit_peruser` bigint,
  `max_discount` double,
  KEY `fk_voucher_1` (`shop_id`),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_voucher_shop_1` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`)
);

CREATE TABLE `voucher_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `voucher_id` bigint,
  `user_id` bigint,
  PRIMARY KEY (`id`),
  KEY `fk_voucher_user_1` (`user_id`),
  KEY `fk_voucher_user_2` (`voucher_id`),
  CONSTRAINT `fk_voucher_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_voucher_user_2` FOREIGN KEY (`voucher_id`) REFERENCES `voucher` (`id`)
);

CREATE TABLE `bill` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `shop_id` bigint DEFAULT NULL,
  `voucher_id` bigint DEFAULT NULL,
  `total_origin` decimal(10,0) DEFAULT NULL,
  `total_voucher` decimal(10,0) DEFAULT NULL,
  `total_payment` decimal(10,0) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `code` varchar(50) NOT NULL,
  `expired_code` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_bill` (`user_id`),
  KEY `fk_voucher_bill` (`voucher_id`),
  KEY `fk_shop_bill` (`shop_id`),
  CONSTRAINT `fk_shop_bill` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`),
  CONSTRAINT `fk_user_bill` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_voucher_bill` FOREIGN KEY (`voucher_id`) REFERENCES `voucher` (`id`)
);

CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `bill_id` bigint DEFAULT NULL,
  `payment_info` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_bill_payment` (`bill_id`),
  CONSTRAINT `fk_bill_payment` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`id`)
);

CREATE TABLE `bill_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint DEFAULT NULL,
  `shop_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `bill_id` bigint DEFAULT NULL,
  `amount` bigint DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_product_bill_item` (`product_id`),
  KEY `fk_shop_bill_item` (`shop_id`),
  KEY `fk_user_bill_item` (`user_id`),
  KEY `fk_bill_bill_item` (`bill_id`),
  CONSTRAINT `fk_bill_bill_item` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`id`),
  CONSTRAINT `fk_product_bill_item` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `fk_shop_bill_item` FOREIGN KEY (`shop_id`) REFERENCES `shop` (`id`),
  CONSTRAINT `fk_user_bill_item` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);