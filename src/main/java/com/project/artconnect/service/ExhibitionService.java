package com.project.artconnect.service;

import com.project.artconnect.model.Exhibition;

import java.util.List;

public interface ExhibitionService {
    public List<Exhibition> findAll();
    public void save(Exhibition exhibition);
    public void update(Exhibition exhibition);
    public void delete(Exhibition exhibition);
}
