ALTER TABLE user ADD COLUMN is_login TINYINT(1) DEFAULT 0,
                ADD COLUMN device_token TEXT NULL;