ALTER TABLE bill ADD COLUMN payment_id bigint;
ALTER TABLE bill ADD CONSTRAINT `pk_payment_bill_3` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`);
ALTER TABLE payment DROP FOREIGN KEY `fk_bill_payment`;
ALTER TABLE payment DROP COLUMN bill_id;