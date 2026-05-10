package com.project.artconnect.dao;

import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Workshop;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GalleryDao {
    Gallery findById(int id) throws SQLException;

    List<Gallery> findAll() throws SQLException;

    void save(Gallery gallery) throws SQLException;

    void update(Gallery gallery) throws SQLException;

    void delete(Gallery gallery) throws SQLException;
}
