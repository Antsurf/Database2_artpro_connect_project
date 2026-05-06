drop trigger if exists stop_delete_with_if_in_workshop;

delimiter //
CREATE trigger stop_delete_with_if_in_workshop
BEFORE DELETE ON communitymember
FOR EACH ROW 
BEGIN 
	declare community_member_in_booking INT; 
	select count(*) into community_member_in_booking from booking where cm_id = old.cm_id;
    if(community_member_in_booking > 0) then
		signal SQLSTATE '45000'
		SET MESSAGE_TEXT ='Community member has booked a workshop already';
	end if;
END 
//
delimiter ;

drop trigger if exists sold_artwork;

delimiter //
CREATE trigger sold_artwork
BEFORE UPDATE ON artworks
FOR EACH ROW 
BEGIN 
    if(new.artwork_status = "sold") then
		set new.gallery_id = NULL;
	end if;
END 
//
delimiter ;

drop trigger if exists check_date_workshop;


delimiter //
CREATE trigger check_date_workshop
BEFORE INSERT ON workshop
FOR EACH ROW 
BEGIN 
    if(new.workshop_date < curdate()) then
		signal SQLSTATE '45000'
		SET MESSAGE_TEXT ='The date of the new workshop is wrong';
	end if;
END 
//
delimiter ;

drop trigger if exists check_price_artwork;

delimiter //
CREATE trigger check_price_artwork
BEFORE INSERT ON artworks
FOR EACH ROW 
BEGIN 
    if(new.artwork_price <= 0) then
		signal SQLSTATE '45000'
		SET MESSAGE_TEXT ='The price cannot be negative';
	end if;
END 
//
delimiter ;


DELIMITER //
CREATE TRIGGER t_workshop_available
BEFORE INSERT ON BOOKING
FOR EACH ROW 
Begin
    DECLARE countBooking int;
    DECLARE maxPlace int;
    SELECT count(booking_paymentStatus) INTO countBooking FROM Booking
    WHERE workshop_id = NEW.workshop_id;

    SELECT workshop_maxParticipants INTO maxPlace FROM Workshop
    WHERE workshop_id = NEW.workshop_id;

    if countBooking >= maxPlace then
        signal SQLSTATE '45000'
            SET MESSAGE_TEXT ='The workshop is full';
    end if;
END //
DELIMITER ;
