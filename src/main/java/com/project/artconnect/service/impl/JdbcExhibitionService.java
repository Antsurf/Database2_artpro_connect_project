package com.project.artconnect.service.impl;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.persistence.JdbcExhibitionDao;
import com.project.artconnect.service.ExhibitionService;

import java.util.List;

public class JdbcExhibitionService implements ExhibitionService {
    private final ExhibitionDao exhibitionDao = new JdbcExhibitionDao();
    @Override
    public List<Exhibition> findAll() {
        return exhibitionDao.findAll();
    }

    @Override
    public void save(Exhibition exhibition) {
        exhibitionDao.save(exhibition);
    }

    @Override
    public void update(Exhibition exhibition) {
        exhibitionDao.update(exhibition);
    }

    @Override
    public void delete(String title) {
        exhibitionDao.delete(title);
    }
}
