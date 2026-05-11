package com.project.artconnect.service.impl;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.ArtistService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.persistence.JdbcArtistDao;
import com.project.artconnect.util.ConnectionManager;

public class JdbcArtistService implements ArtistService{
    // made by using mainly what is done in "inmemoryartistservice.java"
    private final ArtistDao artistDao = new JdbcArtistDao();

    @Override
    public List<Artist> getAllArtists() {
        return artistDao.findAll();
    }

    @Override
    public Optional<Artist> getArtistByName(String name) {
        // to handle null values
        return Optional.ofNullable(artistDao.findByName(name));
    }

    @Override
    public void createArtist(Artist artist) {
        artistDao.save(artist);
    }

    @Override
    public void updateArtist(Artist artist) {
        artistDao.update(artist);
    }

    @Override
    public void deleteArtist(String name) {
        getArtistByName(name).ifPresent(a->artistDao.delete(a.getId()));
    }

    @Override
    // not very clear because no parameters, I guess return all types of all disciplines ?
    public List<Discipline> getAllDisciplines() {
        List<Discipline> disciplines = new ArrayList<>();
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "SELECT * FROM discipline";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                disciplines.add(new Discipline(resultSet.getInt("discipline_id"), resultSet.getString("discipline_name")));
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return disciplines;
    }

        // same as in "inmemory", just added a check if query or discipline is empty
    @Override
    public List<Artist> searchArtists(String query, String disciplineName, String city) {
        return artistDao.findAll().stream()
            .filter(a->query == null || a.getName().toLowerCase().contains(query.toLowerCase()))
                .filter(a -> city == null || city.isEmpty()
                        || a.getCity().equalsIgnoreCase(city))
                .filter(a -> disciplineName == null
                        || a.getDisciplines().stream()
                        .anyMatch(d -> d.getName().equalsIgnoreCase(disciplineName)))
                .collect(Collectors.toList());
    }

}
