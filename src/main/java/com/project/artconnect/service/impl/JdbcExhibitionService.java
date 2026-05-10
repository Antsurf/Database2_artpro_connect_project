package com.project.artconnect.service.impl;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.model.Exhibition;

import java.util.List;

public class JdbcExhibitionService implements ExhibitionDao {
    private final ExhibitionDao exhibitionDao = new JdbcExhibitionService();
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
