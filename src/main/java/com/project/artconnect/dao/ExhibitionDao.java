package com.project.artconnect.dao;

import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Exhibition;

import java.sql.SQLException;
import java.util.List;

public interface ExhibitionDao {
    List<Exhibition> findAll();

    List<Exhibition> findAllByGallery(int id);

    Exhibition findById(int id);

    List<Artwork> getListArtworks(int id) ;

    void save(Exhibition exhibition);

    void update(Exhibition exhibition);

    void delete(Exhibition exhibition) ;
}
