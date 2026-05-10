package com.project.artconnect.service.impl;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.dao.GalleryDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.persistence.JdbcExhibitionDao;
import com.project.artconnect.persistence.JdbcGalleryDao;
import com.project.artconnect.service.GalleryService;

import java.util.List;
import java.util.Optional;

public class JdbcGalleryService implements GalleryService {
    private final GalleryDao galleryDao = new JdbcGalleryDao();
    @Override
    public List<Gallery> getAllGalleries() {
        return galleryDao.findAll();
    }

    @Override
    public Optional<Gallery> getGalleryByName(String name) {
        return galleryDao.findAll().stream().filter(g->g.getName().equalsIgnoreCase(name)).findFirst();
    }

    // TODO: once Remi implemented JdbcGalleryDao, check if the result is loaded inside gallery->exhibitions
    @Override
    public List<Exhibition> getExhibitionsByGallery(Gallery gallery) {
        ExhibitionDao exhibitionDao = new JdbcExhibitionDao();
        return exhibitionDao.findAllByGallery(gallery.getId());
    }
}
