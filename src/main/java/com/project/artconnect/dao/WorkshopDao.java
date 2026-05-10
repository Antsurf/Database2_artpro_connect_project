package com.project.artconnect.dao;


import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Workshop;

import java.sql.SQLException;
import java.util.List;

public interface WorkshopDao {

    Workshop findById(int id);

    List<Workshop> findAll();

    void save(Workshop workshop);

    void update(Workshop workshop);

    void delete(Workshop workshop);

}