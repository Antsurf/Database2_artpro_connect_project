DROP database if EXISTS ARTPROJECT;
CREATE DATABASE ARTPROJECT;
USE ARTPROJECT;
CREATE TABLE Exhibitions(
   exhibition_id INT auto_increment,
   exhibition_title VARCHAR(50),
   exhibition_startDate DATE,
   exhibition_endDate DATE,
   exhibition_description VARCHAR(50),
   exhibition_curatorName VARCHAR(50),
   exhibition_theme VARCHAR(50),
   PRIMARY KEY(exhibition_id)
);

CREATE TABLE Artworks(
   artwork_id INT auto_increment,
   artwork_title VARCHAR(50),
   artwork_creationYear INT,
   artwork_type VARCHAR(50),
   artwork_medium VARCHAR(50),
   artwork_dimensions VARCHAR(50),
   artwork_description VARCHAR(50),
   artwork_price DECIMAL(15,2),
   artwork_status VARCHAR(50),
   artwork_tags VARCHAR(50),
   exhibition_id INT,
   PRIMARY KEY(artwork_id),
   FOREIGN KEY(exhibition_id) REFERENCES Exhibitions(exhibition_id) ON DELETE CASCADE
);

CREATE TABLE Artist(
   artist_id INT auto_increment,
   artist_name VARCHAR(50),
   artist_bio VARCHAR(50),
   artist_birthYear INT,
   artist_contactEmail VARCHAR(50),
   artist_phone VARCHAR(50),
   artist_city VARCHAR(50),
   artist_website VARCHAR(50),
   artist_socialMedia VARCHAR(50),
   artist_isActive BOOLEAN,
   PRIMARY KEY(artist_id)
);

CREATE TABLE CommunityMember(
   cm_id INT auto_increment,
   cm_name VARCHAR(50),
   cm_email VARCHAR(50),
   cm_birthYear INT,
   cm_phone VARCHAR(50),
   cm_city VARCHAR(50),
   cm_membershipType VARCHAR(50),
   PRIMARY KEY(cm_id)
);

CREATE TABLE Discipline(
   discipline_id INT auto_increment,
   discipline_name VARCHAR(50),
   PRIMARY KEY(discipline_id)
);

CREATE TABLE Address(
   address_id INT auto_increment,
   city_name VARCHAR(50),
   postal_code INT,
   street_name VARCHAR(50),
   country_name VARCHAR(50),
   street_number INT,
   PRIMARY KEY(address_id)
);

CREATE TABLE Galleries(
   gallery_id INT auto_increment,
   gallery_name VARCHAR(50) NOT NULL,
   gallery_ownerName VARCHAR(50) NOT NULL,
   gallery_openingHour VARCHAR(50) NOT NULL,
   gallery_contactPhone VARCHAR(50),
   gallery_website VARCHAR(50) NOT NULL,
   gallery_rating DECIMAL(15,2) NOT NULL,
   address_id int NOT NULL,
   PRIMARY KEY(gallery_id),
   UNIQUE(address_id),
   FOREIGN KEY(address_id) REFERENCES Address(address_id) ON DELETE CASCADE
);

CREATE TABLE Workshop(
   workshop_id INT auto_increment,
   workshop_title VARCHAR(50),
   workshop_date DATETIME,
   workshop_durationMinutes INT,
   workshop_maxParticipants INT,
   workshop_price DECIMAL(15,2),
   workshop_description VARCHAR(50),
   workshop_level VARCHAR(50),
   artist_id INT NOT NULL,
   gallery_id INT NOT NULL,
   PRIMARY KEY(workshop_id),
   FOREIGN KEY(artist_id) REFERENCES Artist(artist_id) ON DELETE CASCADE,
   FOREIGN KEY(gallery_id) REFERENCES Galleries(gallery_id) ON DELETE CASCADE
);

CREATE TABLE created(
   artwork_id INT,
   artist_id INT,
   PRIMARY KEY(artwork_id, artist_id),
   FOREIGN KEY(artwork_id) REFERENCES Artworks(artwork_id) ON DELETE CASCADE,
   FOREIGN KEY(artist_id) REFERENCES Artist(artist_id) ON DELETE CASCADE
);

CREATE TABLE presents(
   gallery_id INT,
   exhibition_id INT,
   PRIMARY KEY(gallery_id, exhibition_id),
   FOREIGN KEY(gallery_id) REFERENCES Galleries(gallery_id) ON DELETE CASCADE,
   FOREIGN KEY(exhibition_id) REFERENCES Exhibitions(exhibition_id) ON DELETE CASCADE
);

CREATE TABLE booking(
   workshop_id INT,
   cm_id INT,
   booking_bookingDate DATETIME,
   booking_paymentStatus VARCHAR(50),
   PRIMARY KEY(workshop_id, cm_id),
   FOREIGN KEY(workshop_id) REFERENCES Workshop(workshop_id) ON DELETE CASCADE,
   FOREIGN KEY(cm_id) REFERENCES CommunityMember(cm_id) ON DELETE CASCADE
);

CREATE TABLE is_specialized_in(
   artist_id INT,
   discipline_id INT,
   PRIMARY KEY(artist_id, discipline_id),
   FOREIGN KEY(artist_id) REFERENCES Artist(artist_id) ON DELETE CASCADE,
   FOREIGN KEY(discipline_id) REFERENCES Discipline(discipline_id) ON DELETE CASCADE
);

CREATE TABLE favorite(
   cm_id INT,
   discipline_id INT,
   PRIMARY KEY(cm_id, discipline_id),
   FOREIGN KEY(cm_id) REFERENCES CommunityMember(cm_id) ON DELETE CASCADE,
   FOREIGN KEY(discipline_id) REFERENCES Discipline(discipline_id) ON DELETE CASCADE
);

CREATE TABLE review(
   artwork_id INT,
   cm_id INT,
   review_rating DECIMAL(15,2),
   review_comment VARCHAR(50),
   review_date DATE,
   review_type VARCHAR(50),
   PRIMARY KEY(artwork_id, cm_id),
   FOREIGN KEY(artwork_id) REFERENCES Artworks(artwork_id) ON DELETE CASCADE,
   FOREIGN KEY(cm_id) REFERENCES CommunityMember(cm_id) ON DELETE CASCADE
);



USE artproject;

-- ==========================================================
-- STEP 1: INDEPENDENT ENTITIES
-- ==========================================================

-- Addresses
INSERT INTO Address (address_id, city_name, postal_code, street_name, country_name, street_number) VALUES
                                                                                                       (1, 'Paris', 75001, 'Rue de Rivoli', 'France', 99),
                                                                                                       (2, 'London', 10001, 'Great Russell St', 'UK', 20),
                                                                                                       (3, 'New York', 10001, '5th Ave', 'USA', 1000),
                                                                                                       (4, 'Paris', 75004, 'Place Georges-Pompidou', 'France', 1),
                                                                                                       (5, 'Florence', 50122, 'Piazzale degli Uffizi', 'Italy', 6),
                                                                                                       (6, 'Berlin', 10117, 'Bodestrasse', 'Germany', 1),
                                                                                                       (7, 'Madrid', 28014, 'Calle de Ruiz de Alarcon', 'Spain', 23),
                                                                                                       (8, 'Tokyo', 11000, 'Uenokoen', 'Japan', 7),
                                                                                                       (9, 'Amsterdam', 1071, 'Museumstraat', 'Netherlands', 1),
                                                                                                       (10, 'Bilbao', 48009, 'Avenida Abandoibarra', 'Spain', 2);

-- Artists
INSERT INTO Artist (artist_id, artist_name, artist_bio, artist_birthYear, artist_contactEmail, artist_phone, artist_city, artist_website, artist_socialMedia, artist_isActive) VALUES
                                                                                                                                                                                   (1, 'Claude Monet', 'Founder of Impressionism', 1840, 'monet@art.com', '0102030405', 'Paris', 'monet.fr', '@monet_art', TRUE),
                                                                                                                                                                                   (2, 'Leonardo da Vinci', 'High Renaissance polymath', 1452, 'leo@renaissance.it', '0607080910', 'Florence', 'davinci.it', '@the_leo', TRUE),
                                                                                                                                                                                   (3, 'Auguste Rodin', 'Modern sculptor', 1840, 'rodin@sculpt.fr', '0142345678', 'Paris', 'rodin-museum.fr', '@rodin_sculpt', TRUE),
                                                                                                                                                                                   (4, 'Vincent van Gogh', 'Post-Impressionist painter', 1853, 'vincent@starry.nl', '0198765432', 'Arles', 'vangogh.nl', '@vincent_vg', TRUE),
                                                                                                                                                                                   (5, 'Michelangelo', 'Italian sculptor and painter', 1475, 'mich@vatican.va', '0505050505', 'Florence', 'buonarroti.it', '@michel_art', TRUE),
                                                                                                                                                                                   (6, 'Frida Kahlo', 'Surrealist portrait artist', 1907, 'frida@coyoacan.mx', '555123456', 'Mexico City', 'fkahlo.com', '@frida_forever', TRUE),
                                                                                                                                                                                   (7, 'Katsushika Hokusai', 'Ukiyo-e painter and printmaker', 1760, 'hokusai@edo.jp', '81031234', 'Edo', 'hokusai-prints.jp', NULL, FALSE),
                                                                                                                                                                                   (8, 'Salvador Dali', 'Surrealist icon', 1904, 'dali@surreal.es', '349312345', 'Figueres', 'dali.es', '@soft_watches', TRUE),
                                                                                                                                                                                   (9, 'Georgia OKeeffe', 'Mother of American modernism', 1887, 'georgia@okeeffe.org', '1212555000', 'Santa Fe', 'okeeffe.org', '@okeeffe_art', TRUE),
                                                                                                                                                                                   (10, 'Banksy', 'Anonymous street artist', 1974, 'pest@control.uk', '0770090000', 'Bristol', 'banksy.co.uk', '@banksy', TRUE);

-- Disciplines
INSERT INTO Discipline (discipline_id, discipline_name) VALUES
                                                            (1, 'Painting'), (2, 'Sculpture'), (3, 'Digital Art'), (4, 'Photography'), (5, 'Oil Painting'),
                                                            (6, 'Impressionism'), (7, 'Street Art'), (8, 'Ukiyo-e'), (9, 'Surrealism'), (10, 'Abstract');

-- Community Members
INSERT INTO CommunityMember (cm_id, cm_name, cm_email, cm_birthYear, cm_phone, cm_city, cm_membershipType) VALUES
                                                                                                               (1, 'Alice Martin', 'alice@mail.com', 1995, '0611223344', 'Paris', 'Premium'),
                                                                                                               (2, 'Bob Smith', 'bob@mail.com', 1988, '0622334455', 'London', 'Standard'),
                                                                                                               (3, 'Charlie Durand', 'charlie@mail.com', 2000, '0633445566', 'Lyon', 'Student'),
                                                                                                               (4, 'Diana Prince', 'diana@mail.com', 1992, '0644556677', 'New York', 'Premium'),
                                                                                                               (5, 'Eve Online', 'eve@mail.com', 1985, '0655667788', 'Berlin', 'Professional'),
                                                                                                               (6, 'Frank Castle', 'frank@mail.com', 1975, '0666778899', 'New York', 'Standard'),
                                                                                                               (7, 'Grace Hopper', 'grace@navy.mil', 1906, '0677889900', 'Arlington', 'Professional'),
                                                                                                               (8, 'Heidi Klum', 'heidi@fashion.de', 1973, '0688990011', 'Hamburg', 'Premium'),
                                                                                                               (9, 'Ivan Drago', 'ivan@boxe.ru', 1960, '0699001122', 'Moscow', 'Standard'),
                                                                                                               (10, 'Judy Hopps', 'judy@zpd.gov', 2016, '0700112233', 'Zootopia', 'Student');

-- ==========================================================
-- STEP 2: LINKED ENTITIES (LEVEL 1)
-- ==========================================================

-- Galleries
INSERT INTO Galleries (gallery_id, gallery_name, gallery_ownerName, gallery_openingHour, gallery_contactPhone, gallery_website, gallery_rating, address_id) VALUES
                                                                                                                                                                (1, 'Louvre Art House', 'Jean-Luc Martinez', '09:00-18:00', '0140205050', 'louvre.fr', 4.9, 1),
                                                                                                                                                                (2, 'The British Gallery', 'Nicholas Cullinan', '10:00-20:00', '02073238000', 'britishmuseum.org', 4.7, 2),
                                                                                                                                                                (3, 'Metropolitan Hub', 'Max Hollein', '10:00-21:00', '2125357710', 'metmuseum.org', 4.8, 3),
                                                                                                                                                                (4, 'Pompidou Center', 'Laurent Le Bon', '11:00-21:00', '0144781233', 'centrepompidou.fr', 4.5, 4),
                                                                                                                                                                (5, 'Uffizi Gallery', 'Eike Schmidt', '08:15-18:30', '0552388651', 'uffizi.it', 4.9, 5),
                                                                                                                                                                (6, 'Prado Hall', 'Miguel Falomir', '10:00-20:00', '34913302800', 'museodelprado.es', 4.8, 7),
                                                                                                                                                                (7, 'Rijksmuseum', 'Taco Dibbits', '09:00-17:00', '310206747000', 'rijksmuseum.nl', 4.7, 9),
                                                                                                                                                                (8, 'Guggenheim Bilbao', 'Juan Ignacio Vidarte', '10:00-19:00', '34944359000', 'guggenheim-bilbao.eus', 4.6, 10);

-- Exhibitions
INSERT INTO Exhibitions (exhibition_id, exhibition_title, exhibition_startDate, exhibition_endDate, exhibition_description, exhibition_curatorName, exhibition_theme) VALUES
                                                                                                                                                                          (1, 'Renaissance Revival', '2026-05-01', '2026-08-31', 'Exploring Italian roots', 'Marco Rossi', 'Classic Renaissance'),
                                                                                                                                                                          (2, 'Sculpting the Soul', '2026-06-15', '2026-09-15', 'Modern sculpture journey', 'Jane Doe', 'Modern & Classical Sculpture'),
                                                                                                                                                                          (3, 'Impressionist Dreams', '2026-04-10', '2026-07-20', 'Light and color', 'Paul Durand', 'Light and Color'),
                                                                                                                                                                          (4, 'Digital Frontiers', '2026-07-01', '2026-10-01', 'Art in the age of AI', 'S. Jobs', 'Technology'),
                                                                                                                                                                          (5, 'Florence Golden Age', '2026-03-01', '2026-06-01', 'Uffizi masterworks', 'C. Medici', 'History'),
                                                                                                                                                                          (6, 'Velasquez & Friends', '2026-01-15', '2026-04-15', 'Spanish Golden Age', 'J. Goya', 'Baroque'),
                                                                                                                                                                          (7, 'Vermeer Masterpieces', '2026-02-01', '2026-05-30', 'Dutch domestic life', 'H. Van Rijn', 'Dutch Golden Age'),
                                                                                                                                                                          (8, 'Urban Rebellion', '2026-08-01', '2026-11-30', 'Street art evolution', 'Banksy', 'Modern Rebellion');

-- ==========================================================
-- STEP 3: LINKED ENTITIES (LEVEL 2) - ENFORCING ENUMS
-- ==========================================================

-- Artworks: status {forSale, sold, exhibited}
INSERT INTO Artworks (artwork_id, artwork_title, artwork_creationYear, artwork_type, artwork_medium, artwork_dimensions, artwork_description, artwork_price, artwork_status, artwork_tags, exhibition_id) VALUES
                                                                                                                                                                                                              (101, 'Mona Lisa', 1503, 'Painting', 'Oil on Poplar', '77x53cm', 'Iconic portrait', 999999.99, 'exhibited', 'Renaissance', 1),
                                                                                                                                                                                                              (102, 'The Thinker', 1904, 'Sculpture', 'Bronze', '186cm', 'Contemplation', 50000.00, 'exhibited', 'Modern', 2),
                                                                                                                                                                                                              (103, 'Water Lilies', 1919, 'Painting', 'Oil on Canvas', '200x200cm', 'Nature', 12000.00, 'forSale', 'Impressionism', 3),
                                                                                                                                                                                                              (104, 'David', 1504, 'Sculpture', 'Marble', '517cm', 'Biblical hero', 888888.88, 'exhibited', 'Marble', 5),
                                                                                                                                                                                                              (105, 'Starry Night', 1889, 'Painting', 'Oil on Canvas', '73x92cm', 'Night sky vision', 75000.00, 'exhibited', 'Post-Impressionism', 3),
                                                                                                                                                                                                              (106, 'The Two Fridas', 1939, 'Painting', 'Oil on Canvas', '173x173cm', 'Self-portrait', 45000.00, 'exhibited', 'Surrealism', 4),
                                                                                                                                                                                                              (107, 'Great Wave', 1831, 'Print', 'Woodblock', '25x37cm', 'Japanese icon', 15000.00, 'exhibited', 'Japan', 8),
                                                                                                                                                                                                              (108, 'Melting Clocks', 1931, 'Painting', 'Oil on Canvas', '24x33cm', 'Surreal time', 60000.00, 'forSale', 'Surrealism', 4),
                                                                                                                                                                                                              (109, 'Girl with Balloon', 2002, 'Street Art', 'Stencil', '80x60cm', 'Symbol of hope', 25000.00, 'sold', 'Banksy', 8),
                                                                                                                                                                                                              (110, 'The Milkmaid', 1658, 'Painting', 'Oil on Canvas', '45x41cm', 'Domestic scene', 30000.00, 'exhibited', 'Dutch', 7);

-- Workshops
INSERT INTO Workshop (workshop_id, workshop_title, workshop_date, workshop_durationMinutes, workshop_maxParticipants, workshop_price, workshop_description, workshop_level, artist_id, gallery_id) VALUES
                                                                                                                                                                                                       (201, 'Oil Painting Masters', '2026-05-10 14:00:00', 120, 15, 150.00, 'Classic techniques', 'Advanced', 2, 1),
                                                                                                                                                                                                       (202, 'Impressionist Light', '2026-05-15 10:00:00', 180, 20, 120.00, 'Capturing nature', 'Intermediate', 1, 3),
                                                                                                                                                                                                       (203, 'Modern Sculpture', '2026-06-01 09:00:00', 240, 10, 200.00, 'Clay and bronze', 'Beginner', 3, 2),
                                                                                                                                                                                                       (204, 'Anatomy Sketching', '2026-05-20 15:00:00', 150, 12, 180.00, 'Human form', 'Advanced', 5, 5),
                                                                                                                                                                                                       (205, 'Stencil Basics', '2026-08-10 18:00:00', 90, 30, 50.00, 'Street art 101', 'Beginner', 10, 8);

-- ==========================================================
-- STEP 4: RELATIONSHIP TABLES - ENFORCING ENUMS
-- ==========================================================

-- Booking: paymentStatus {PENDING, PAID, CANCELLED}
INSERT INTO booking (workshop_id, cm_id, booking_bookingDate, booking_paymentStatus) VALUES
                                                                                         (201, 1, '2026-04-10 09:00:00', 'PAID'),
                                                                                         (201, 4, '2026-04-11 10:30:00', 'PAID'),
                                                                                         (202, 1, '2026-04-12 11:00:00', 'PENDING'),
                                                                                         (203, 2, '2026-04-13 14:20:00', 'CANCELLED'),
                                                                                         (204, 5, '2026-04-14 16:00:00', 'PAID'),
                                                                                         (205, 6, '2026-07-01 12:00:00', 'PAID'),
                                                                                         (205, 10, '2026-07-02 09:15:00', 'PAID');

-- Work Creation
INSERT INTO created (artwork_id, artist_id) VALUES
                                                (101, 2), (102, 3), (103, 1), (104, 5), (105, 4), (106, 6), (107, 7), (108, 8), (109, 10), (110, 1);

-- Gallery Presentations
INSERT INTO presents (gallery_id, exhibition_id) VALUES
                                                     (1, 1 ), (2, 2), (3, 3), (4, 4), (5, 5), (6, 6), (7, 7), (8, 8);

-- Specializations
INSERT INTO is_specialized_in (artist_id, discipline_id) VALUES
                                                             (1, 1), (1, 6), (2, 1), (2, 5), (3, 2), (5, 2), (6, 9), (10, 7);

-- Favorites
INSERT INTO favorite (cm_id, discipline_id) VALUES
                                                (1, 1), (1, 6), (2, 2), (3, 3), (4, 5), (6, 7), (10, 8);

-- Reviews
INSERT INTO review (artwork_id, cm_id, review_rating, review_comment, review_date, review_type) VALUES
                                                                                                    (101, 1, 5.0, 'Timeless masterpiece', '2026-05-02', 'Professional'),
                                                                                                    (103, 3, 4.0, 'Stunning use of color', '2026-04-15', 'Student'),
                                                                                                    (109, 2, 3.5, 'Modern and powerful', '2026-09-01', 'Visitor');

drop user if exists "project";
create user "project" IDENTIFIED BY "projectPW";

GRANT ALL PRIVILEGES
ON artproject.*
TO 'project'
WITH GRANT OPTION;

drop user if exists "admin";
create user "admin" IDENTIFIEd BY "admin";

GRANT ALL PRIVILEGES
ON artproject.*
TO 'admin'
WITH GRANT OPTION;

GRANT EXECUTE ON artproject.* TO "admin";


USE artproject;

# Index on the status of artwork
CREATE INDEX index_art_status ON Artworks(artwork_status);

# Index on the price of workshops
CREATE  INDEX index_workshop_price ON Workshop(workshop_price);

# Index on workshop level
CREATE  INDEX index_workshop_level ON Workshop(workshop_level);

# Index on workshop level & date
CREATE INDEX index_workshop_level_date ON Workshop(workshop_level,workshop_date);

USE artproject;

# ========== STORED FUNCTIONS ===============

# Return the number of participants currently booked for a
# given workshop. This could be written as a procedure but the goal is to have quick reports

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




# ================ STORED PROCEDURES =================

# Marks an artwork as 'sold'.
# The trigger `sold_artwork` will automatically set gallery_id to NULL on the same update.


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
        a.exhibition_id   AS exhibition_id   -- NULL if trigger happened
    FROM Artworks a
    WHERE a.artwork_id = p_artwork_id;
END //
DELIMITER ;



# Returns all upcoming workshops held at a gallery located in the given city.

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
             JOIN presents p   ON e.exhibition_id = p.exhibition_id
             JOIN Galleries g  ON p.gallery_id = g.gallery_id
             JOIN Address   ad ON g.address_id = ad.address_id
    WHERE ad.city_name = p_city
      AND e.exhibition_endDate >= CURDATE()
    ORDER BY e.exhibition_startDate;
END //
DELIMITER ;


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
             JOIN presents p   ON e.exhibition_id = p.exhibition_id
             JOIN Galleries g  ON p.gallery_id = g.gallery_id
             JOIN Address   ad ON g.address_id = ad.address_id
    WHERE LOWER(e.exhibition_theme) = LOWER(p_theme)
    ORDER BY e.exhibition_startDate;
END //
DELIMITER ;


drop procedure if exists create_user_communitymember;

DELIMITER //

CREATE PROCEDURE create_user_communitymember(IN _username VARCHAR(55), IN _password VARCHAR(200))
BEGIN
	DECLARE _HOST CHAR(14) DEFAULT '@\'localhost\'';
    
    SET _username := CONCAT('\'', REPLACE(TRIM(`_username`), CHAR(39), CONCAT(CHAR(92), CHAR(39))), '\''),
    _password := CONCAT('\'', REPLACE(`_password`, CHAR(39), CONCAT(CHAR(92), CHAR(39))), '\'');
    
    SET @sql := CONCAT('CREATE USER ', _username, _HOST, ' IDENTIFIED BY ', _password);
    PREPARE stmt FROM @sql;SELECT User FROM mysql.user;
    EXECUTE stmt;
    
    SET @sql := CONCAT('GRANT SELECT ON artproject.* to', _username, _HOST);
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    
    SET @sql := CONCAT('GRANT EXECUTE ON artproject.* to', _username, _HOST);
	PREPARE stmt FROM @sql;
    EXECUTE stmt;
    
    SET @sql := CONCAT('GRANT INSERT ON artproject.booking to', _username, _HOST);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    
    SET @sql := CONCAT('GRANT INSERT ON artproject.review to', _username, _HOST);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
END //

DELIMITER ;

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


delimiter //
CREATE trigger sold_artwork
BEFORE UPDATE ON artworks
FOR EACH ROW 
BEGIN 
    if(new.artwork_status = "sold") then
		set new.exhibition_id = NULL;
	end if;
END 
//
delimiter ;



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
    if is_workshop_full(NEW.workshop_id) = 1 then
        signal SQLSTATE '45000'
            SET MESSAGE_TEXT ='The workshop is full';
    end if;
END //
DELIMITER ;

USE artproject;

CREATE OR REPLACE VIEW v_workshop_instructor_informations AS
SELECT * FROM workshop AS w
JOIN artist AS a USING(artist_id);

CREATE OR REPLACE VIEW v_address AS
SELECT 
    city_name AS city, 
    postal_code AS zip, 
    street_name AS street, 
    country_name AS country, 
    street_number AS "number"
FROM Address;

CREATE OR REPLACE VIEW v_artist AS
SELECT 
    artist_name AS name, 
    artist_bio AS bio, 
    artist_birthYear AS birth_year, 
    artist_contactEmail AS email, 
    artist_phone AS phone, 
    artist_city AS city, 
    artist_website AS website, 
    artist_socialMedia AS social, 
    artist_isActive AS is_active 
FROM Artist;

CREATE OR REPLACE VIEW v_discipline AS
SELECT 
    discipline_name AS discipline
FROM Discipline;

CREATE OR REPLACE VIEW v_communitymember AS
SELECT 
    cm_name AS name, 
    cm_email AS email, 
    cm_birthYear AS birth_year, 
    cm_phone AS phone, 
    cm_city AS city, 
    cm_membershipType AS membership 
FROM CommunityMember;

CREATE OR REPLACE VIEW v_galleries AS
SELECT 
    gallery_name AS name, 
    gallery_ownerName AS owner, 
    gallery_openingHour AS hours, 
    gallery_contactPhone AS phone, 
    gallery_website AS website, 
    gallery_rating AS rating 
FROM Galleries;

CREATE OR REPLACE VIEW v_exhibitions AS
SELECT 
    exhibition_title AS title, 
    exhibition_startDate AS start_date, 
    exhibition_endDate AS end_date, 
    exhibition_description AS description, 
    exhibition_curatorName AS curator, 
    exhibition_theme AS theme 
FROM Exhibitions;

CREATE OR REPLACE VIEW v_artworks AS
SELECT 
    artwork_title AS title, 
    artwork_creationYear AS year, 
    artwork_type AS type, 
    artwork_medium AS medium, 
    artwork_dimensions AS dimensions, 
    artwork_description AS description, 
    artwork_price AS price, 
    artwork_status AS status, 
    artwork_tags AS tags 
FROM Artworks;

CREATE OR REPLACE VIEW v_workshop AS
SELECT 
    workshop_title AS title, 
    workshop_date AS date, 
    workshop_durationMinutes AS duration, 
    workshop_maxParticipants AS max_capacity, 
    workshop_price AS price, 
    workshop_description AS description, 
    workshop_level AS level 
FROM Workshop;

CREATE OR REPLACE VIEW v_booking AS
SELECT 
    booking_bookingDate AS booking_date, 
    booking_paymentStatus AS payment_status 
FROM booking;

CREATE OR REPLACE VIEW v_review AS
SELECT 
    review_rating AS rating, 
    review_comment AS comment, 
    review_date AS date, 
    review_type AS type 
FROM review;

CREATE OR REPLACE VIEW v_sales_performance_report AS
SELECT 
    art.artwork_title AS title,
    a.artist_name AS artist,
    g.gallery_name AS gallery,
    ad.city_name AS city,
    art.artwork_price AS price,
    art.artwork_status AS status
FROM Artworks art
JOIN created c ON art.artwork_id = c.artwork_id
JOIN Artist a ON c.artist_id = a.artist_id
JOIN presents p ON p.exhibition_id = art.exhibition_id
JOIN Galleries g ON p.gallery_id = g.gallery_id
JOIN Address ad ON g.address_id = ad.address_id
WHERE art.artwork_status = 'sold';
