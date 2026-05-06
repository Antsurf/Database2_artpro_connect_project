USE artproject;

# ========== STORED FUNCTIONS ===============

# Return the number of participants currently booked for a
# given workshop. This could be written as a procedure but the goal is to have quick reports
DROP FUNCTION IF EXISTS get_workshop_participant_count;

DELIMITER //
CREATE FUNCTION get_workshop_participant_count(p_workshop_id INT)
    RETURNS INT
    READS SQL DATA
BEGIN
    DECLARE v_count INT;
    SELECT COUNT(*) INTO v_count
    FROM booking
    WHERE workshop_id = p_workshop_id;
    RETURN v_count;
END //
DELIMITER ;

# Test:
SELECT get_workshop_participant_count(1);
SELECT workshop_title, get_workshop_participant_count(workshop_id) AS participants FROM Workshop;


# Returns the average review rating for a given artwork,
# or NULL if no reviews exist yet.

DROP FUNCTION IF EXISTS get_average_artwork_rating;

DELIMITER //
CREATE FUNCTION get_average_artwork_rating(p_artwork_id INT)
    RETURNS DECIMAL(5,2)
    READS SQL DATA
BEGIN
    DECLARE v_avg DECIMAL(5,2);
    SELECT AVG(review_rating) INTO v_avg
    FROM review
    WHERE artwork_id = p_artwork_id;
    RETURN v_avg;
END //
DELIMITER ;

# Test:
SELECT get_average_artwork_rating(1);
SELECT artwork_title, get_average_artwork_rating(artwork_id) AS avg_rating FROM Artworks;



# Returns 1 (TRUE) if the workshop has reached max capacity,
# 0 (FALSE) otherwise.
DROP FUNCTION IF EXISTS is_workshop_full;
DELIMITER //
CREATE FUNCTION is_workshop_full(p_workshop_id INT)
    RETURNS BOOLEAN
    READS SQL DATA
BEGIN
    DECLARE v_booked INT;
    DECLARE v_max    INT;
    SELECT COUNT(*)                INTO v_booked FROM booking  WHERE workshop_id = p_workshop_id;
    SELECT workshop_maxParticipants INTO v_max   FROM Workshop WHERE workshop_id = p_workshop_id;
    RETURN v_booked >= v_max;
END //
DELIMITER ;

# Test:
SELECT is_workshop_full(1);
SELECT workshop_id, workshop_title, is_workshop_full(workshop_id) AS is_full FROM Workshop;



# ================ STORED PROCEDURES =================

# Marks an artwork as 'sold'.
# The trigger `sold_artwork` will automatically set gallery_id to NULL on the same update.

DROP PROCEDURE IF EXISTS sell_artwork;

DELIMITER //
CREATE PROCEDURE sell_artwork(IN p_artwork_id INT)
BEGIN
    -- artwork must exist
    IF NOT EXISTS (SELECT 1 FROM Artworks WHERE artwork_id = p_artwork_id) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'sell_artwork: artwork not found.';
    END IF;

    -- artwork must not already be sold
    IF EXISTS (
        SELECT 1 FROM Artworks
        WHERE artwork_id = p_artwork_id AND artwork_status = 'sold'
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'sell_artwork: artwork is already sold.';
    END IF;

    -- trigger sold_artwork fires here and sets gallery_id = NULL
    UPDATE Artworks
    SET artwork_status = 'sold'
    WHERE artwork_id = p_artwork_id;

    -- Return the updated artwork to confirm
    SELECT
        a.artwork_id,
        a.artwork_title   AS title,
        a.artwork_price   AS price,
        a.artwork_status  AS status,
        a.gallery_id      AS gallery_id   -- NULL if trigger happened
    FROM Artworks a
    WHERE a.artwork_id = p_artwork_id;
END //
DELIMITER ;

CALL sell_artwork(1);



# Returns all upcoming workshops held at a gallery located in the given city.

DROP PROCEDURE IF EXISTS get_workshops_by_city;

DELIMITER //
CREATE PROCEDURE get_workshops_by_city(IN p_city VARCHAR(50))
BEGIN
    SELECT
        w.workshop_id,
        w.workshop_title           AS title,
        w.workshop_date            AS date,
        w.workshop_durationMinutes AS duration_min,
        w.workshop_maxParticipants AS max_participants,
        w.workshop_price           AS price,
        w.workshop_level           AS level,
        g.gallery_name             AS gallery,
        ad.city_name               AS city,
        ad.street_name             AS street,
        ad.street_number           AS street_number,
        ar.artist_name             AS instructor
    FROM Workshop w
             JOIN Galleries g  ON w.gallery_id = g.gallery_id
             JOIN Address   ad ON g.address_id = ad.address_id
             JOIN Artist    ar ON w.artist_id  = ar.artist_id
    WHERE ad.city_name = p_city
      AND w.workshop_date >= CURDATE()
    ORDER BY w.workshop_date;
END //
DELIMITER ;

# Test:
CALL get_workshops_by_city('Paris');


# Returns all ongoing or upcoming exhibitions at a gallery located in the given city.

DROP PROCEDURE IF EXISTS get_exhibitions_by_city;

DELIMITER //
CREATE PROCEDURE get_exhibitions_by_city(IN p_city VARCHAR(50))
BEGIN
    SELECT
        e.exhibition_id,
        e.exhibition_title       AS title,
        e.exhibition_startDate   AS start_date,
        e.exhibition_endDate     AS end_date,
        e.exhibition_description AS description,
        e.exhibition_curatorName AS curator,
        e.exhibition_theme       AS theme,
        g.gallery_name           AS gallery,
        ad.city_name             AS city
    FROM Exhibitions e
             JOIN Galleries g  ON e.gallery_id = g.gallery_id
             JOIN Address   ad ON g.address_id = ad.address_id
    WHERE ad.city_name = p_city
      AND e.exhibition_endDate >= CURDATE()
    ORDER BY e.exhibition_startDate;
END //
DELIMITER ;

# Test:
CALL get_exhibitions_by_city('Lyon');


# Returns all exhibitions whose theme is given in parameters with their gallery and city.

DROP PROCEDURE IF EXISTS get_exhibitions_by_theme;

DELIMITER //
CREATE PROCEDURE get_exhibitions_by_theme(IN p_theme VARCHAR(50))
BEGIN
    SELECT
        e.exhibition_id,
        e.exhibition_title       AS title,
        e.exhibition_startDate   AS start_date,
        e.exhibition_endDate     AS end_date,
        e.exhibition_description AS description,
        e.exhibition_curatorName AS curator,
        e.exhibition_theme       AS theme,
        g.gallery_name           AS gallery,
        ad.city_name             AS city
    FROM Exhibitions e
             JOIN Galleries g  ON e.gallery_id = g.gallery_id
             JOIN Address   ad ON g.address_id = ad.address_id
    WHERE LOWER(e.exhibition_theme) = LOWER(p_theme)
    ORDER BY e.exhibition_startDate;
END //
DELIMITER ;

# Test:
CALL get_exhibitions_by_theme('impressionism');
CALL get_exhibitions_by_theme('surrealism');
CALL get_exhibitions_by_theme('modern art');


