package com.project.artconnect.service.impl;

import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.persistence.JdbcArtworkDao;
import com.project.artconnect.service.ArtworkService;
import java.util.*;

public class JdbcArtworkService implements ArtworkService {

    // NO NEED OF INIT
    private final ArtworkDao artworkDao = new JdbcArtworkDao();

    @Override
    public List<Artwork> getAllArtworks() {
        return artworkDao.findAll();
    }

    @Override
    // not in ArtworkDAO interface
    public Optional<Artwork> getArtworkByTitle(String title) {
        /*
        .findFirst() is mandatory as it wraps in an Optional<Artwork> type
        instead of a Stream<Artwork> (given by .stream())
        .equalsIgnoreCase() also important so that Leo = leo = LEO

        Optional type : returns an optional container either full (if artwork) or empty
        good to avoid returning null that can create crash

        not very opti bc if a lot of lines, quicker to do a sql query rather than this
         */
        return artworkDao.findAll().stream().filter(a->a.getTitle().equalsIgnoreCase(title)).findFirst();
    }

    @Override
    public List<Artwork> getArtworksByArtist(Artist artist) {
        return artworkDao.findByArtistName(artist.getName());
    }

    @Override
    public void createArtwork(Artwork artwork) {
        System.out.println("nearly in");
        artworkDao.save(artwork);

    }

    @Override
    public void updateArtwork(Artwork artwork) {
        artworkDao.update(artwork);
    }

    @Override
    public void deleteArtwork(String title) {
        artworkDao.delete(title);
    }
}
