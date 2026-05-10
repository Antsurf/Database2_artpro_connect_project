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

        int galleryId = rs.getInt("gallery_id");
        if (galleryId > 0) {
            GalleryDao galleryDao = new JdbcGalleryDao();
            exhibition.setGallery(galleryDao.findById(galleryId));
        }

        ArtworkDao artworkDao = new JdbcArtworkDao();
        List<Artwork> list = artworkDao.findAll();
        exhibition.setArtworks(list);

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
            throw new RuntimeException(e);
        }
        return exhibition;
    }

    @Override
    public List<Exhibition> findAll(){
        List<Exhibition> list = new ArrayList<>();
        String sql = "SELECT * FROM Exhibitions";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Exhibition> findAllByGallery(int id){
        List<Exhibition> list = new ArrayList<>();
        String sql = "SELECT * FROM Exhibitions WHERE gallery_id = ?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void getListArtworks(Exhibition exhibition){
        exhibition.getArtworks();
    }


    @Override
    public void save(Exhibition exhibition){
        String sql = "INSERT INTO Exhibitions (exhibition_id, gallery_id, exhibition_title, exhibition_startDate, exhibition_endDate, exhibition_description, exhibition_curatorName, exhibition_theme) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, exhibition.getId());
            ps.setString(3,exhibition.getTitle());
            ps.setString(4,exhibition.getStartDate().toString());
            ps.setString(5,exhibition.getEndDate().toString());
            ps.setString(6,exhibition.getDescription());
            ps.setString(7,exhibition.getCuratorName());
            ps.setString(8,exhibition.getTheme());

            Gallery gallery= null;
            GalleryDao galleryDao = new JdbcGalleryDao();
            gallery = galleryDao.findById(exhibition.getGallery().getId());
            if (gallery == null) {
                galleryDao.save(exhibition.getGallery());
            }
            ps.setInt(2, exhibition.getGallery().getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Exhibition exhibition){
        String sql = "UPDATE Exhibitions SET exhibition_id=?, gallery_id=?, exhibition_title=?, exhibition_startDate=?, exhibition_endDate=?, exhibition_description=?, exhibition_curatorName=?, exhibition_theme=? WHERE  exhibition_id=?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, exhibition.getId());
            ps.setInt(2, exhibition.getGallery().getId());
            ps.setString(3,exhibition.getTitle());
            ps.setString(4,exhibition.getStartDate().toString());
            ps.setString(5,exhibition.getEndDate().toString());
            ps.setString(6,exhibition.getDescription());
            ps.setString(7,exhibition.getCuratorName());
            ps.setString(8,exhibition.getTheme());
            ps.setInt(9,exhibition.getId());

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
            throw new RuntimeException(e);
        }

    }
}
