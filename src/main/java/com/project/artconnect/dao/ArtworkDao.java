package com.project.artconnect.dao;

import com.project.artconnect.model.Artwork;
import java.util.List;

public interface ArtworkDao {
    List<Artwork> findAll();

    Artwork findById(int id);

    public List<Artwork> findByArtistId(int id);

    void save(Artwork artwork);

    void update(Artwork artwork);

    boolean inDatabase(Artwork artwork);

    void delete(String title);

    List<Artwork> findByArtistName(String artistName);
}
