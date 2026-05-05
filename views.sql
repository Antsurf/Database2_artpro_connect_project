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
JOIN Galleries g ON art.gallery_id = g.gallery_id
JOIN Address ad ON g.address_id = ad.address_id
WHERE art.artwork_status = 'sold';

SELECT * FROM v_workshop_instructor_informations;
SELECT * FROM v_address;
SELECT * FROM v_artist;
SELECT * FROM v_discipline;
SELECT * FROM v_communitymember;
SELECT * FROM v_galleries;
SELECT * FROM v_exhibitions;
SELECT * FROM v_artworks;
SELECT * FROM v_workshop;
SELECT * FROM v_booking;
SELECT * FROM v_review;
SELECT * FROM v_sales_performance_report;