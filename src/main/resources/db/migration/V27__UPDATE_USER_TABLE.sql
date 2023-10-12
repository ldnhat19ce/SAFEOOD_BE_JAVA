ALTER TABLE user ADD COLUMN shop_id bigint NULL,
                ADD FOREIGN KEY fk_user_shop (shop_id) REFERENCES shop(id)