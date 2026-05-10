package com.project.artconnect.dao;

import com.project.artconnect.model.Discipline;

public interface DisciplineDAO {

    Discipline save(Discipline discipline);

    void saveLink(int artistId, Discipline discipline);
}
