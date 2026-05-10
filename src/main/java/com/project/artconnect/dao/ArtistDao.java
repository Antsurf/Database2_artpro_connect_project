package com.project.artconnect.dao;

import com.project.artconnect.model.Artist;
import java.util.List;

/**
 * Data Access Object for Artist entity.
 */
public interface ArtistDao {
    List<Artist> findAll();

    Artist findByName(String name);

    Artist findById(int id);

    void save(Artist artist);

    void update(Artist artist);

    void delete(int artistId);

    List<Artist> findByCity(String city);
}
