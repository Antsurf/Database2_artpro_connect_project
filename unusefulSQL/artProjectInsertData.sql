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
