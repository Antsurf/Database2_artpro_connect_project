package com.project.artconnect.persistence;
import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Address;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;


public class JdbcWorkshopDao implements WorkshopDao {

    private Workshop mapRow(ResultSet rs) throws SQLException {
        Workshop workshop = new Workshop();
        workshop.setId(rs.getInt("workshop_id"));
        workshop.setTitle(rs.getString("workshop_title"));
        workshop.setDescription(rs.getString("workshop_description"));
        workshop.setLevel(rs.getString("workshop_level"));
        workshop.setDurationMinutes(rs.getInt("workshop_durationMinutes"));
        workshop.setMaxParticipants(rs.getInt("workshop_maxParticipants"));
        workshop.setPrice(rs.getDouble("workshop_price"));

        workshop.setDate(rs.getObject("workshop_date", LocalDateTime.class));

        int artistId = rs.getInt("artist_id");
        int galleryId = rs.getInt("gallery_id");


        JdbcArtistDao jdbcArtistDao = new JdbcArtistDao();

        Artist artist = jdbcArtistDao.findById(artistId);
        workshop.setInstructor(artist);


        JdbcGalleryDao jdbcGalleryDao = new JdbcGalleryDao();
        Gallery gallery = jdbcGalleryDao.findById(galleryId);

        workshop.setGallery(gallery);

        return workshop;
    }

    @Override
    public Workshop findById(int id) {
        Workshop workshop = new Workshop();
        String sql = "SELECT * FROM Workshop WHERE workshop_id = ?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                workshop = mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return workshop;
    }

    @Override
    public List<Workshop> findAll() {
        List<Workshop> workshops = new ArrayList<>();

        String sql = "SELECT * FROM Workshop";
        try (Connection conn = ConnectionManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                workshops.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return workshops;
    }

    @Override
    public void save(Workshop workshop) {
        String sql = "INSERT INTO Workshop (workshop_id, workshop_title, workshop_date, workshop_durationMinutes, workshop_maxParticipants, workshop_price, workshop_description, workshop_level, artist_id, gallery_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, workshop.getId());
            ps.setString(2, workshop.getTitle());
            ps.setObject(3, workshop.getDate());
            ps.setInt(4, workshop.getDurationMinutes());
            ps.setInt(5, workshop.getMaxParticipants());
            ps.setDouble(6, workshop.getPrice());
            ps.setString(7, workshop.getDescription());
            ps.setString(8, workshop.getLevel());

            Artist artistExist = null;
            JdbcArtistDao jdbcArtistDao = new JdbcArtistDao();
            artistExist = jdbcArtistDao.findById(workshop.getInstructor().getId());
            if  (artistExist == null) {
                jdbcArtistDao.save(workshop.getInstructor());
            }

            Gallery galleryExist = null;
            JdbcGalleryDao jdbcGalleryDao = new JdbcGalleryDao();
            galleryExist = jdbcGalleryDao.findById(workshop.getGallery().getId());
            if (galleryExist == null) {
                jdbcGalleryDao.save(workshop.getGallery());
            }

            ps.setInt(9, workshop.getInstructor().getId());
            ps.setInt(10, workshop.getGallery().getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Workshop workshop){
        String sql = "UPDATE workshop set workshop_title=?, workshop_date=?, workshop_durationMinutes=?, workshop_maxParticipants=?, workshop_price=?, workshop_description=?, workshop_level=?, artist_id=?, gallery_id=? WHERE workshop_id = ?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, workshop.getTitle());
            ps.setObject(2, workshop.getDate());
            ps.setInt(3, workshop.getDurationMinutes());
            ps.setInt(4, workshop.getMaxParticipants());
            ps.setDouble(5, workshop.getPrice());
            ps.setString(6, workshop.getDescription());
            ps.setString(7, workshop.getLevel());
            ps.setInt(8, workshop.getId());
            ps.setInt(9, workshop.getId());

            Artist artistExist = null;
            JdbcArtistDao jdbcArtistDao = new JdbcArtistDao();
            artistExist = jdbcArtistDao.findById(workshop.getInstructor().getId());
            if  (artistExist == null) {
                jdbcArtistDao.save(workshop.getInstructor());
            }
            Gallery galleryExist = null;
            JdbcGalleryDao jdbcGalleryDao = new JdbcGalleryDao();
            galleryExist = jdbcGalleryDao.findById(workshop.getGallery().getId());
            if (galleryExist == null) {
                jdbcGalleryDao.save(workshop.getGallery());
            }

            ps.setInt(10, workshop.getInstructor().getId());
            ps.setInt(11, workshop.getGallery().getId());


            ps.setInt(12, workshop.getId());

            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(Workshop workshop){
        String sql = "DELETE FROM workshop WHERE Workshop_id = ?;";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,workshop.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
