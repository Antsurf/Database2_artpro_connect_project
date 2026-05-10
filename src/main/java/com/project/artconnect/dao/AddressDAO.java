package com.project.artconnect.dao;

import com.project.artconnect.model.Address;
import java.sql.SQLException;
import java.util.List;

public interface AddressDAO {

    Address findById(int id) throws SQLException;

    List<Address> findAll() throws SQLException;

    void save(Address address) throws SQLException;

    void update(Address address) throws SQLException;

    void delete(Address address) throws SQLException;
}
