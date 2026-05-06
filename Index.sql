USE artproject;

# Index on the status of artwork
CREATE INDEX index_art_status ON Artworks(artwork_status);
SHOW INDEX FROM artworks;

# Index on the price of workshops
CREATE  INDEX index_workshop_price ON Workshop(workshop_price);

# Index on workshop level
CREATE  INDEX index_workshop_level ON Workshop(workshop_level);

# Register to all beginner workshop in a city, if two have the same date-hour, then cancel the transaction
DELIMITER //
CREATE PROCEDURE learn_painting(p_cm_id int)
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE cursor_id INT;
    
    DECLARE ws CURSOR FOR 
    SELECT workshop_id FROM WORKSHOP WHERE workshop_date >= current_date();
    
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;	
	START TRANSACTION;
		
	OPEN ws;
	read_loop : LOOP
		FETCH ws INTO cursor_id;
		if done then
			LEAVE read_loop;
		end if;
		INSERT INTO booking (workshop_id, cm_id, booking_bookingDate, booking_paymentStatus) VALUES (cursor_id, p_cm_id, current_date(), 'PAID');
	end LOOP;
	CLOSE ws;
	COMMIT;
	
end //
DELIMITER ;

CALL learn_painting(15);