package com.project.artconnect.persistence;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.util.ConnectionManager;

import java.net.CookieHandler;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        // Load artist (instructor)
        JdbcArtistDao jdbcArtistDao = new JdbcArtistDao();
        workshop.setInstructor(jdbcArtistDao.findById(rs.getInt("artist_id")));

        // Load gallery (location)
        JdbcGalleryDao jdbcGalleryDao = new JdbcGalleryDao();
        workshop.setGallery(jdbcGalleryDao.findById(rs.getInt("gallery_id")));

        return workshop;
    }

    @Override
    public Workshop findById(int id) {
        String sql = "SELECT * FROM Workshop WHERE workshop_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Workshop> findAll() {
        List<Workshop> workshops = new ArrayList<>();
        String sql = "SELECT * FROM Workshop";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) workshops.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return workshops;
    }

    @Override
    public void save(Workshop workshop) {
        String sql = "INSERT INTO Workshop ( workshop_title, workshop_date, " +
                "workshop_durationMinutes, workshop_maxParticipants, workshop_price, " +
                "workshop_description, workshop_level, artist_id, gallery_id) " +
                "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Ensure artist exists
            JdbcArtistDao jdbcArtistDao = new JdbcArtistDao();
            if (jdbcArtistDao.findById(workshop.getInstructor().getId()) == null) {
                jdbcArtistDao.save(workshop.getInstructor());
            }

            // Ensure gallery exists
            JdbcGalleryDao jdbcGalleryDao = new JdbcGalleryDao();
            if (jdbcGalleryDao.findById(workshop.getGallery().getId()) == null) {
                jdbcGalleryDao.save(workshop.getGallery());
            }

            ps.setString(1, workshop.getTitle());
            ps.setObject(2, workshop.getDate());
            ps.setInt(3, workshop.getDurationMinutes());
            ps.setInt(4, workshop.getMaxParticipants());
            ps.setDouble(5, workshop.getPrice());
            ps.setString(6, workshop.getDescription());
            ps.setString(7, workshop.getLevel());
            ps.setInt(8, workshop.getInstructor().getId());
            ps.setInt(9, workshop.getGallery().getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Workshop workshop) {
        String sql = "UPDATE Workshop SET " +
                "workshop_title = ?, " +          // 1
                "workshop_date = ?, " +            // 2
                "workshop_durationMinutes = ?, " + // 3
                "workshop_maxParticipants = ?, " + // 4
                "workshop_price = ?, " +           // 5
                "workshop_description = ?, " +     // 6
                "workshop_level = ?, " +           // 7
                "artist_id = ?, " +                // 8
                "gallery_id = ? " +                // 9
                "WHERE workshop_id = ?";           // 10

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, workshop.getTitle());
            ps.setObject(2, workshop.getDate());
            ps.setInt(3, workshop.getDurationMinutes());
            ps.setInt(4, workshop.getMaxParticipants());
            ps.setDouble(5, workshop.getPrice());
            ps.setString(6, workshop.getDescription());
            ps.setString(7, workshop.getLevel());
            ps.setInt(8, workshop.getInstructor().getId());
            ps.setInt(9, workshop.getGallery().getId());
            ps.setInt(10, workshop.getId());             // WHERE

            // Ensure artist and gallery exist
            JdbcArtistDao jdbcArtistDao = new JdbcArtistDao();
            if (jdbcArtistDao.findById(workshop.getInstructor().getId()) == null) {
                jdbcArtistDao.save(workshop.getInstructor());
            }
            JdbcGalleryDao jdbcGalleryDao = new JdbcGalleryDao();
            if (jdbcGalleryDao.findById(workshop.getGallery().getId()) == null) {
                jdbcGalleryDao.save(workshop.getGallery());
            }

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Workshop workshop) {
        String sql = "DELETE FROM Workshop WHERE workshop_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workshop.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int getNumberOfBooking(Workshop workshop){
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "SELECT get_workshop_participant_count(?) as count";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, Integer.toString(workshop.getId()));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return resultSet.getInt("count");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return -1;
    }

    @Override
    public void registerToWorkshop(Workshop workshop, CommunityMember communityMember){
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "INSERT INTO booking (workshop_id, cm_id, booking_bookingDate, booking_paymentStatus) VALUES (?, ?, current_date(), 'NOT PAID')";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, workshop.getId());
            preparedStatement.setInt(2, communityMember.getId());
            preparedStatement.execute();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

}