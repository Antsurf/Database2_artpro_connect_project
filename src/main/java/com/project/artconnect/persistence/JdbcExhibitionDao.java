package com.project.artconnect.persistence;
import com.project.artconnect.dao.*;
import com.project.artconnect.model.Address;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.persistence.JdbcExhibitionDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JdbcExhibitionDao implements ExhibitionDao{

    private Exhibition mapRow(ResultSet rs) throws SQLException {
        Exhibition exhibition = new Exhibition();
        exhibition.setId(rs.getInt("exhibition_id"));
        exhibition.setTitle(rs.getString("exhibition_title"));

        exhibition.setDescription(rs.getString("exhibition_description"));
        exhibition.setTheme(rs.getString("exhibition_theme"));
        exhibition.setCuratorName(rs.getString("exhibition_curatorName"));
        exhibition.setStartDate(rs.getDate("exhibition_startDate").toLocalDate());
        exhibition.setEndDate(rs.getDate("exhibition_endDate").toLocalDate());

        GalleryDao galleryDao = new JdbcGalleryDao();
        exhibition.setGallery(galleryDao.findById((Integer) rs.getObject("gallery_id")));

        ExhibitionDao exhibitionDao = new JdbcExhibitionDao();
        exhibition.setArtworks(exhibitionDao.getListArtworks(rs.getInt("exhibition_id")));


        return exhibition;
    }

    @Override
    public Exhibition findById(int id){
        Exhibition exhibition = null;
        String sql = "SELECT * FROM Exhibitions WHERE exhibition_id = ?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exhibition = mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            }
        return exhibition;
    }

    @Override
    public List<Exhibition> findAll(){
        List<Exhibition> list = new ArrayList<>();
        String sql = "SELECT e.*, g.* FROM Exhibitions e \n" +
                "JOIN presents p ON e.exhibition_id = p.exhibition_id\n" +
                "JOIN Galleries g ON p.gallery_id = g.gallery_id ";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
                    }
        return list;
    }

    @Override
    public List<Exhibition> findAllByGallery(int id){
        List<Exhibition> list = new ArrayList<>();
        // need to pass the gallery_id to avoid confict in the map row
        String sql = "SELECT e.*, p.gallery_id FROM Exhibitions e " +
                "JOIN presents p ON e.exhibition_id = p.exhibition_id " +
                "WHERE p.gallery_id = ?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            }
        return list;
    }

    @Override
    // get list artworks for a given exhibition
    public List<Artwork> getListArtworks(int id){
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT a.*, e.* FROM artworks a\n" +
                "JOIN exhibitions e ON a.exhibition_id = e.exhibition_id\n WHERE e.exhibition_id = ? ";
        try (Connection conn = ConnectionManager.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                // implemented from jdbcartworkdao.java
                artworks.add(JdbcArtworkDao.mapArtwork(rs));
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return artworks;
    }


    @Override
    public void save(Exhibition exhibition){
        String sql = "INSERT INTO Exhibitions ( gallery_id, exhibition_title, exhibition_startDate, exhibition_endDate, exhibition_description, exhibition_curatorName, exhibition_theme) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,exhibition.getTitle());
            ps.setString(2,exhibition.getStartDate().toString());
            ps.setString(3,exhibition.getEndDate().toString());
            ps.setString(4,exhibition.getDescription());
            ps.setString(5,exhibition.getCuratorName());
            ps.setString(6,exhibition.getTheme());

            Gallery gallery= null;
            GalleryDao galleryDao = new JdbcGalleryDao();
            gallery = galleryDao.findById(exhibition.getGallery().getId());
            if (gallery == null) {
                galleryDao.save(exhibition.getGallery());
            }
            ps.setInt(2, exhibition.getGallery().getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Exhibition exhibition){
        String sql = "UPDATE Exhibitions SET gallery_id=?, exhibition_title=?, exhibition_startDate=?, exhibition_endDate=?, exhibition_description=?, exhibition_curatorName=?, exhibition_theme=? WHERE  exhibition_id=?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, exhibition.getGallery().getId());
            ps.setString(2,exhibition.getTitle());
            ps.setString(3,exhibition.getStartDate().toString());
            ps.setString(4,exhibition.getEndDate().toString());
            ps.setString(5,exhibition.getDescription());
            ps.setString(6,exhibition.getCuratorName());
            ps.setString(7,exhibition.getTheme());
            ps.setInt(8,exhibition.getId());

            Gallery gallery= null;
            GalleryDao galleryDao = new JdbcGalleryDao();
            gallery = galleryDao.findById(exhibition.getGallery().getId());
            if (gallery == null) {
                galleryDao.save(exhibition.getGallery());
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Exhibition exhibition){
        String sql = "DELETE FROM Exhibitions WHERE exhibition_id = ?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, exhibition.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<Exhibition> filterExhibition(String query, String filter) {
        if (query == null || query.isEmpty()) return findAll();
        return findAll().stream()
                .filter(e -> {
                    String valueToCompare = switch (filter) {
                        case "theme" -> e.getTheme();
                        case "city" -> e.getGallery().getAddress().getCity_name();
                        case "title" -> e.getTitle();
                        default -> "";
                    };
                    return valueToCompare != null && valueToCompare.toLowerCase().contains(query.toLowerCase());
                })
                .collect(Collectors.toList());
    }
}
