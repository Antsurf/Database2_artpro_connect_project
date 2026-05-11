package com.project.artconnect.persistence;

import com.project.artconnect.dao.AddressDAO;
import com.project.artconnect.model.Address;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcAddressDAO implements AddressDAO {

    private Address mapRow(ResultSet rs) throws SQLException{
        Address address = new Address();
        address.setId(rs.getInt("address_id"));
        address.setStreet_number(rs.getInt("street_number"));
        address.setPostal_code(rs.getInt("postal_code"));
        address.setStreet_name(rs.getString("street_name"));
        address.setCity_name(rs.getString("city_name"));
        address.setCountry_name(rs.getString("country_name"));

        return address;
    }

    @Override
    public Address findById(int id) throws SQLException {
        Address address = null;
        String sql = "SELECT * FROM Address WHERE address_id = ?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                address = mapRow(rs);
            }
        }
        return address;
    }

    @Override
    public List<Address> findAll() throws SQLException{
        List<Address> list = new ArrayList<>();
        String sql = "SELECT * FROM Address";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public void save(Address address) throws SQLException{
        String sql = "INSERT INTO Address (city_name, postal_code, street_name, country_name, street_number) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, address.getCity_name());
            ps.setInt(2, address.getPostal_code());
            ps.setString(3, address.getStreet_name());
            ps.setString(4, address.getCountry_name());
            ps.setInt(5, address.getStreet_number());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Address address) throws SQLException{
        String sql = "UPDATE Address set  city_name=?, postal_code=?, street_name=?, country_name=?, street_number=? WHERE address_id = ?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, address.getCity_name());
            ps.setInt(2, address.getPostal_code());
            ps.setString(3, address.getStreet_name());
            ps.setString(4, address.getCountry_name());
            ps.setInt(5, address.getStreet_number());
            ps.setInt(6, address.getId());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Address address) throws SQLException{
        String sql = "DELETE FROM Address WHERE address_id = ?";
        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, address.getId());
            ps.executeUpdate();
        }
    }

}


