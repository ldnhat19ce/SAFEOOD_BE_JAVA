ALTER TABLE payment ADD COLUMN amount double;
ALTER TABLE payment ADD COLUMN txnref varchar(50);
ALTER TABLE payment ADD COLUMN response_code varchar(10);
