package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Artist;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JdbcArtworkDao implements ArtworkDao {


    // mapping to avoid code repetition
    public static Artwork mapArtwork(ResultSet rs) throws SQLException {
        Artwork artwork = new Artwork();
        artwork.setId(rs.getInt("artwork_id"));
        artwork.setTitle(rs.getString("artwork_title"));
        artwork.setCreationYear(rs.getInt("artwork_creationYear"));
        artwork.setType(rs.getString("artwork_type"));
        artwork.setMedium(rs.getString("artwork_medium"));
        artwork.setDimensions(rs.getString("artwork_dimensions"));
        artwork.setDescription(rs.getString("artwork_description"));
        artwork.setPrice(rs.getDouble("artwork_price"));
        // Map DB status string to Java enum
        String statusStr = rs.getString("artwork_status");
        if (statusStr != null) {
            switch (statusStr.toLowerCase()) {
                case "sold":      artwork.setStatus(Artwork.Status.SOLD);      break;
                case "exhibited": artwork.setStatus(Artwork.Status.EXHIBITED); break;
                default:          artwork.setStatus(Artwork.Status.FOR_SALE);  break;
            }
        }
        return artwork;
    }

    private Artist mapArtist(ResultSet rs) throws SQLException {
        Artist artist = new Artist();
        artist.setId(rs.getInt("artist_id"));
        artist.setName(rs.getString("artist_name"));
        artist.setBio(rs.getString("artist_bio"));
        artist.setBirthYear(rs.getInt("artist_birthYear"));
        artist.setContactEmail(rs.getString("artist_contactEmail"));
        artist.setPhone(rs.getString("artist_phone"));
        artist.setCity(rs.getString("artist_city"));
        artist.setWebsite(rs.getString("artist_website"));
        artist.setSocialMedia(rs.getString("artist_socialMedia"));
        artist.setActive(rs.getBoolean("artist_isActive"));
        return artist;
    }

    @Override
    public List<Artwork> findAll() {
        List<Artwork> artworks = new ArrayList<>();
        // query that joins artist to really get all informations about an artwork.
        // because of 1.n and 0.n relation, we need to use created table (cf. Looping scheme)
        String sql = "SELECT aw.*, ar.* FROM Artworks aw " +
                "JOIN created c ON aw.artwork_id = c.artwork_id " +
                "JOIN Artist ar ON c.artist_id = ar.artist_id";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Artwork artwork = mapArtwork(rs);
                // check if there is an artist linked to this painting (security check to avoid crash)
                // normally, there is always an artist linked to a table (1.n)
                if (rs.getObject("artist_id") != null) {
                    artwork.setArtist(mapArtist(rs));
                }
                artworks.add(artwork);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return artworks;
    }

    @Override
    public List<Artwork> findByArtistName(String artistName) {
        List<Artwork> artworks = new ArrayList<>();
        // same query as above juste with a specified id
        String sql = "SELECT aw.*, ar.* FROM Artworks aw " +
                "JOIN created c ON aw.artwork_id = c.artwork_id " +
                "JOIN Artist ar ON c.artist_id = ar.artist_id " +
                "WHERE ar.artist_name = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, artistName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Artwork artwork = mapArtwork(rs);
                    artwork.setArtist(mapArtist(rs));
                    artworks.add(artwork);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return artworks;
    }

    @Override
    public List<Artwork> findByArtistId(int id) {
        List<Artwork> artworks = new ArrayList<>();
        // same query as above juste with a specified id
        String sql = "SELECT aw.*, ar.* FROM Artworks aw " +
                "JOIN created c ON aw.artwork_id = c.artwork_id " +
                "JOIN Artist ar ON c.artist_id = ar.artist_id " +
                "WHERE ar.artist_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, Integer.toString(id));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Artwork artwork = mapArtwork(rs);
                    artwork.setArtist(mapArtist(rs));
                    artworks.add(artwork);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return artworks;
    }

    @Override
    public boolean inDatabase(Artwork artwork){
        boolean inDB = false;
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "SELECT * FROM artworks WHERE artwork_id = ? or artwork_title =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, Integer.toString(artwork.getId()));
            preparedStatement.setString(2, artwork.getTitle());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                inDB = true;
            }
            return inDB;
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return inDB;
    }


    @Override
    public Artwork findById(int artworkId) {
        Artwork artwork = new Artwork();
        // same query as above juste with a specified id
        String sql = "SELECT aw.*, ar.* FROM Artworks aw " +
                "JOIN created c ON aw.artwork_id = c.artwork_id " +
                "JOIN Artist ar ON c.artist_id = ar.artist_id " +
                "WHERE aw.artwork_id = ?";
        try (Connection conn = ConnectionManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, String.valueOf(artworkId));
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                artwork = mapArtwork(rs);
                artwork.setArtist(mapArtist(rs));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return artwork;
    }

    @Override
    public void save(Artwork artwork) {
        String sql = "INSERT INTO Artworks (artwork_title, artwork_creationYear, artwork_type, " +
                "artwork_medium, artwork_dimensions, artwork_description, artwork_price, artwork_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             // use of RETURN_GENERATED_KEYS because when we had in created table, we need the ID
             // so e can do ps.getGeneratedKeys() to get the key that was generated and add it to created
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, artwork.getTitle());
            ps.setInt(2, artwork.getCreationYear());
            ps.setString(3, artwork.getType());
            ps.setString(4, artwork.getMedium());
            ps.setString(5, artwork.getDimensions());
            ps.setString(6, artwork.getDescription());
            ps.setDouble(7, artwork.getPrice());
            ps.setString(8, artwork.getStatus() != null ? artwork.getStatus().name().toLowerCase() : "for_sale");
            ps.executeUpdate();

            // Link to artist in the 'created' join table (once again security check that we have an artist)
            if (artwork.getArtist() != null) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int newId = keys.getInt(1);
                        String linkSql = "INSERT INTO created (artwork_id, artist_id) VALUES (?, ?)";
                        try (PreparedStatement linkPs = conn.prepareStatement(linkSql)) {
                            linkPs.setInt(1, newId);
                            linkPs.setInt(2, artwork.getArtist().getId());
                            linkPs.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Artwork artwork) {
        String sql = "UPDATE Artworks SET artwork_title = ?, artwork_creationYear = ?, artwork_type = ?, " +
                "artwork_medium = ?, artwork_dimensions = ?, artwork_description = ?, " +
                "artwork_price = ?, artwork_status = ? WHERE artwork_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, artwork.getTitle());
            ps.setInt(2, artwork.getCreationYear());
            ps.setString(3, artwork.getType());
            ps.setString(4, artwork.getMedium());
            ps.setString(5, artwork.getDimensions());
            ps.setString(6, artwork.getDescription());
            ps.setDouble(7, artwork.getPrice());
            ps.setString(8, artwork.getStatus() != null ? artwork.getStatus().name().toLowerCase() : "for_sale");
            ps.setInt(9, artwork.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(String title) {
        // because of on delete cascade no need to worries about forein keys etc...
        String sql = "DELETE FROM Artworks WHERE artwork_title = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}