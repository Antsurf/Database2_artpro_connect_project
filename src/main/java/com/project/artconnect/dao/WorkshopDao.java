package com.project.artconnect.dao;


import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Workshop;

import java.sql.SQLException;
import java.util.List;

public interface WorkshopDao {

    Workshop findById(int id) throws SQLException;

    List<Workshop> findAll() throws SQLException;

    void save(Workshop workshop) throws SQLException;

    void update(Workshop workshop) throws SQLException;

    void delete(Workshop workshop) throws SQLException;

}