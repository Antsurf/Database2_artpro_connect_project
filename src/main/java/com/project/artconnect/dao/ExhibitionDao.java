package com.project.artconnect.dao;

import com.project.artconnect.model.Exhibition;

import java.sql.SQLException;
import java.util.List;

public interface ExhibitionDao {
    List<Exhibition> findAll() throws SQLException;

    List<Exhibition> findAllByGallery(int id) throws SQLException;

    Exhibition findById(int id) throws SQLException;

    void getListArtworks(Exhibition exhibition) throws SQLException;

    void save(Exhibition exhibition) throws SQLException;

    void update(Exhibition exhibition) throws SQLException;

    void delete(Exhibition exhibition) throws SQLException;
}
