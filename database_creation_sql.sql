CREATE DATABASE ARTPROJECT;
USE ARTPROJECT;

CREATE TABLE Exhibitions( 
   exhibition_id INT, 
   gallery_id INT, 
   exhibition_title VARCHAR(50), 
   exhibition_startDate DATE, 
   exhibition_endDate DATE, 
   exhibition_description VARCHAR(50), 
   exhibition_curatorName VARCHAR(50), 
   exhibition_theme VARCHAR(50), 
   PRIMARY KEY(exhibition_id, gallery_id) 
); 
 
CREATE TABLE Artworks( 
   artwork_id INT, 
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
   gallery_id INT, 
   PRIMARY KEY(artwork_id), 
   FOREIGN KEY(exhibition_id, gallery_id) REFERENCES 
Exhibitions(exhibition_id, gallery_id) 
); 
 
CREATE TABLE Artist( 
   artist_id INT, 
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
   cm_id INT, 
   cm_name VARCHAR(50), 
   cm_email VARCHAR(50), 
   cm_birthYear INT, 
   cm_phone VARCHAR(50), 
   cm_city VARCHAR(50), 
   cm_membershipType VARCHAR(50), 
   PRIMARY KEY(cm_id) 
); 
 
CREATE TABLE Discipline( 
   discipline_id INT, 
   discipline_name VARCHAR(50), 
   PRIMARY KEY(discipline_id) 
); 
 
CREATE TABLE Address( 
   address_id VARCHAR(50), 
   city_name VARCHAR(50), 
   postal_code INT, 
   street_name VARCHAR(50), 
   country_name VARCHAR(50), 
   street_number INT, 
   PRIMARY KEY(address_id) 
); 
 
CREATE TABLE Galleries( 
   gallery_id INT, 
   gallery_name VARCHAR(50) NOT NULL, 
   gallery_ownerName VARCHAR(50) NOT NULL, 
   gallery_openingHour VARCHAR(50) NOT NULL, 
   gallery_contactPhone VARCHAR(50), 
   gallery_website VARCHAR(50) NOT NULL, 
   gallery_rating DECIMAL(15,2) NOT NULL, 
   address_id VARCHAR(50) NOT NULL, 
   PRIMARY KEY(gallery_id), 
   UNIQUE(address_id), 
   FOREIGN KEY(address_id) REFERENCES Address(address_id) 
); 
 
CREATE TABLE Workshop( 
   workshop_id INT, 
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
   FOREIGN KEY(artist_id) REFERENCES Artist(artist_id), 
   FOREIGN KEY(gallery_id) REFERENCES Galleries(gallery_id) 
); 
 
CREATE TABLE created( 
   artwork_id INT, 
   artist_id INT, 
   PRIMARY KEY(artwork_id, artist_id), 
   FOREIGN KEY(artwork_id) REFERENCES Artworks(artwork_id), 
   FOREIGN KEY(artist_id) REFERENCES Artist(artist_id) 
); 
 
CREATE TABLE presents( 
   gallery_id INT, 
   exhibition_id INT, 
   gallery_id_1 INT, 
   PRIMARY KEY(gallery_id, exhibition_id, gallery_id_1), 
   FOREIGN KEY(gallery_id) REFERENCES Galleries(gallery_id), 
   FOREIGN KEY(exhibition_id, gallery_id_1) REFERENCES 
Exhibitions(exhibition_id, gallery_id) 
); 
 
CREATE TABLE booking( 
   workshop_id INT, 
   cm_id INT, 
   booking_bookingDate DATETIME, 
   booking_paymentStatus VARCHAR(50), 
   PRIMARY KEY(workshop_id, cm_id), 
   FOREIGN KEY(workshop_id) REFERENCES Workshop(workshop_id), 
   FOREIGN KEY(cm_id) REFERENCES CommunityMember(cm_id) 
); 
 
CREATE TABLE is_specialized_in( 
   artist_id INT, 
   discipline_id INT, 
   PRIMARY KEY(artist_id, discipline_id), 
   FOREIGN KEY(artist_id) REFERENCES Artist(artist_id), 
   FOREIGN KEY(discipline_id) REFERENCES Discipline(discipline_id) 
); 
 
CREATE TABLE favorite( 
   cm_id INT, 
   discipline_id INT, 
   PRIMARY KEY(cm_id, discipline_id), 
   FOREIGN KEY(cm_id) REFERENCES CommunityMember(cm_id), 
   FOREIGN KEY(discipline_id) REFERENCES Discipline(discipline_id) 
); 
 
CREATE TABLE review( 
   artwork_id INT, 
   cm_id INT, 
   review_rating DECIMAL(15,2), 
   review_comment VARCHAR(50), 
   review_date DATE, 
   review_type VARCHAR(50), 
   PRIMARY KEY(artwork_id, cm_id), 
   FOREIGN KEY(artwork_id) REFERENCES Artworks(artwork_id), 
   FOREIGN KEY(cm_id) REFERENCES CommunityMember(cm_id) 
); 