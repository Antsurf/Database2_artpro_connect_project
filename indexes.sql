USE artproject;

# Index on the status of artwork
CREATE INDEX index_art_status ON Artworks(artwork_status);
SHOW INDEX FROM artworks;

# Index on the price of workshops
CREATE  INDEX index_workshop_price ON Workshop(workshop_price);

# Index on workshop level
CREATE  INDEX index_workshop_level ON Workshop(workshop_level);

# Index on workshop level & date
CREATE INDEX index_workshop_level_date ON Workshop(workshop_level,workshop_date);

