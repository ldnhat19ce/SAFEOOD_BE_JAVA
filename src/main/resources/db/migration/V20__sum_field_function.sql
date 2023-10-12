CREATE FUNCTION `sum_bill`(
	x bigint, y bigint) RETURNS bigint
    DETERMINISTIC
BEGIN
    DECLARE tmp1 bigint;
    DECLARE tmp2 bigint;
    DECLARE rs bigint;
    SET tmp1 = x;
    SET tmp2 = y;
    SET rs = tmp1+tmp2;
    return rs;
END