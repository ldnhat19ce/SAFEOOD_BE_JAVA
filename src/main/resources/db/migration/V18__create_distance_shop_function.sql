DELIMITER //
CREATE FUNCTION `distance_shop`(
	address_id bigint , x double, y double) RETURNS decimal(10,2)
    DETERMINISTIC
BEGIN
	DECLARE lat1 double;
    DECLARE lon1 double;
    DECLARE lat2 double;
    DECLARE lon2 double;
    DECLARE location_of_shop_x double;
    DECLARE location_of_shop_y double;
    DECLARE dlat double;
    DECLARE dlon double;
    DECLARE a double;
    DECLARE distance decimal;
    SET location_of_shop_x = (SELECT a.x FROM addresses as a WHERE a.id = address_id);
    SET location_of_shop_y = (SELECT a.y FROM addresses as a WHERE a.id = address_id);
    SET lat1 = location_of_shop_x;
    SET lon1 = location_of_shop_y;
    SET lat2 = x;
    SET lon2 = y;

    SET dlat = (lat2 - lat1) * (pi() / 180);
    SET dlon = (lon2 - lon1) * (pi() / 180);

    SET a = (sin(dlat / 2) * sin(dlat / 2) + cos(lat1 * (pi() / 180)) * cos(lat2 * (pi() / 180)) * sin(dlon/2) * sin(dlon/2));
    SET distance = (6371 * (2 * atan2(SQRT(a), SQRT(1-a))));
    return distance;
END