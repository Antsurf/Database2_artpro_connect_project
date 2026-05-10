package com.project.artconnect.dao;

import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Workshop;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GalleryDao {
    Gallery findById(int id) ;

    List<Gallery> findAll();

    void save(Gallery gallery);

    void update(Gallery gallery);

    void delete(Gallery gallery);
}
