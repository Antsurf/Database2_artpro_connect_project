package com.project.artconnect.persistence;
import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.dao.GalleryDao;
import com.project.artconnect.dao.AddressDAO;
import com.project.artconnect.model.Address;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.persistence.JdbcExhibitionDao;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class JdbcGalleryDao implements GalleryDao {

    private Gallery mapRow(ResultSet rs) throws SQLException {
        Gallery gallery = new Gallery();
        gallery.setId(rs.getInt("gallery_id"));
        gallery.setName(rs.getString("gallery_name"));
        gallery.setOwnerName(rs.getString("gallery_ownerName"));
        gallery.setOpeningHours(rs.getString("gallery_openingHour"));
        gallery.setContactPhone(rs.getString("gallery_contactPhone"));
        gallery.setRating(rs.getDouble("gallery_rating"));
        gallery.setWebsite(rs.getString("gallery_website"));


        /** create an infinit loop think that we don't need them
        List<Exhibition> exhibitions = new ArrayList<>();
        ExhibitionDao exhibitionDao = new JdbcExhibitionDao();
        exhibitions = exhibitionDao.findAllByGallery(gallery.getId());
        gallery.setExhibitions(exhibitions);
         **/

        int address_id = rs.getInt("address_id");
        String sql = "SELECT * FROM Address WHERE address_id = ?;";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, address_id);
            ResultSet rsadd = ps.executeQuery();
            if (rsadd.next()) {
                Address address1 = new Address(rsadd.getInt("address_id"), rsadd.getInt("street_number"), rsadd.getInt("postal_code"), rsadd.getString("street_name"), rsadd.getString("city_name"), rsadd.getString("country_name"));
                gallery.setAddress(address1);
            }
        }
        return gallery;
    }

    @Override
    public Gallery findById(int id) throws SQLException {
        Gallery gallery = null;
        String sql = "SELECT * FROM Galleries WHERE gallery_id = ?;";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                gallery = mapRow(rs);
            }
        }
        return gallery;
    }

    @Override
    public List<Gallery> findAll() throws SQLException {
        List<Gallery> galleries = new ArrayList<>();
        String sql = "SELECT * FROM Galleries;";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                galleries.add(mapRow(rs));
            }e
        }
        return galleries;
    }

    @Override
    public void save(Gallery gallery) throws SQLException{
        String sql = "INSERT INTO Galleries (gallery_id, gallery_name, gallery_ownerName, gallery_openingHour, gallery_contactPhone, gallery_website, gallery_rating, address_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection()) {

            JdbcAddressDAO jdbcAddressDao = new JdbcAddressDAO();
            Address existingAddress = jdbcAddressDao.findById(gallery.getAddress().getId());

            System.out.println(existingAddress);

            if (existingAddress == null) { // an address can only have 1 gallery
                jdbcAddressDao.save(gallery.getAddress());

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, gallery.getId());
                ps.setString(2, gallery.getName());
                ps.setString(3, gallery.getOwnerName());
                ps.setString(4, gallery.getOpeningHours());
                ps.setString(5, gallery.getContactPhone());
                ps.setString(6, gallery.getWebsite());
                ps.setDouble(7, gallery.getRating());
                Address address = gallery.getAddress();
                ps.setInt(8, address.getId());

                ps.executeUpdate();
            }

        }
    }

    @Override
    public void update(Gallery gallery) throws SQLException{
        String sql = "UPDATE Galleries set gallery_id=?, gallery_name=?, gallery_ownerName=?, gallery_openingHour=?, gallery_contactPhone=?, gallery_website=?, gallery_rating=?,address_id=? WHERE gallery_id = ?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, gallery.getId());
            ps.setString(2, gallery.getName());
            ps.setString(3, gallery.getOwnerName());
            ps.setString(4, gallery.getOpeningHours());
            ps.setString(5, gallery.getContactPhone());
            ps.setString(6, gallery.getWebsite());
            ps.setDouble(7, gallery.getRating());
            Address address = gallery.getAddress();
            ps.setInt(8, address.getId());
            ps.setInt(9, gallery.getId());

            JdbcAddressDAO jdbcAddressDao = new JdbcAddressDAO();
            jdbcAddressDao.update(address);

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Gallery gallery) throws SQLException{
        String sql = "DELETE FROM Galleries WHERE gallery_id = ?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            Address address = gallery.getAddress();
            ps.setInt(1, address.getId());

            ps.executeUpdate();
        }

    }
}
