package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * JDBC implementation for ArtistDao.
 */
public class JdbcArtistDao implements ArtistDao {

    @Override
    public List<Artist> findAll() {

        List<Artist> artists = new ArrayList<>();
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "SELECT * FROM artist";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("artist_id");
                String name = resultSet.getString("artist_name");
                String bio = resultSet.getString("artist_bio");
                int birthYear = resultSet.getInt("artist_birthYear");
                String mail = resultSet.getString("artist_contactEmail");
                String phone = resultSet.getString("artist_phone");
                String city = resultSet.getString("artist_city");
                String website = resultSet.getString("artist_website");
                String socialMedia = resultSet.getString("artist_socialMedia");
                boolean active = resultSet.getBoolean("artist_isActive");
                artists.add(new Artist(id, name, bio, birthYear, mail, phone, city, website, socialMedia, active));
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return artists;
    }

    @Override
    public Artist findById(int id){
        try(Connection connection = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM artist where artist_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, Integer.toString(id));
            ResultSet resultSet =preparedStatement.executeQuery();
            while(resultSet.next()){
                int artist_id = resultSet.getInt("artist_id");
                String name = resultSet.getString("artist_name");
                String bio = resultSet.getString("artist_bio");
                int birthYear = resultSet.getInt("artist_birthYear");
                String mail = resultSet.getString("artist_contactEmail");
                String phone = resultSet.getString("artist_phone");
                String city = resultSet.getString("artist_city");
                String website = resultSet.getString("artist_website");
                String socialMedia = resultSet.getString("artist_socialMedia");
                boolean active = resultSet.getBoolean("artist_isActive");

                Artist artist = new Artist(id, name, bio, birthYear, mail, phone, city, website, socialMedia, active);
                return artist;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public void save(Artist artist) {

        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "INSERT INTO Artist (artist_name, artist_bio," +
                    " artist_birthYear, artist_contactEmail, artist_phone, artist_city," +
                    " artist_website, artist_socialMedia, artist_isActive) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, artist.getName());
            preparedStatement.setString(2, artist.getBio());
            preparedStatement.setString(3, Integer.toString(artist.getBirthYear()));
            preparedStatement.setString(4, artist.getContactEmail());
            preparedStatement.setString(5, artist.getPhone());
            preparedStatement.setString(6, artist.getCity());
            preparedStatement.setString(7, artist.getWebsite());
            preparedStatement.setString(8, artist.getSocialMedia());
            if(artist.getIsActive()){
                preparedStatement.setString(9, "1");
            }
            else{
                preparedStatement.setString(9, "0");
            }
            preparedStatement.execute();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Artist artist) {
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "UPDATE artist SET artist_name = ?, artist_bio = ?, artist_birthYear = ?, " +
                    "artist_contactEmail = ?, artist_phone = ?, artist_city = ?, artist_website = ?, " +
                    "artist_socialMedia = ?, artist_isActive = ? WHERE artist_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, artist.getName());
            preparedStatement.setString(2, artist.getBio());
            preparedStatement.setString(3, Integer.toString(artist.getBirthYear()));
            preparedStatement.setString(4, artist.getContactEmail());
            preparedStatement.setString(5, artist.getPhone());
            preparedStatement.setString(6, artist.getCity());
            preparedStatement.setString(7, artist.getWebsite());
            preparedStatement.setString(8, artist.getSocialMedia());
            if(artist.getIsActive()){
                preparedStatement.setString(9, "1");
            }
            else{
                preparedStatement.setString(9, "0");
            }
            preparedStatement.setString(10, Integer.toString(artist.getId()));
            preparedStatement.execute();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }



    @Override
    public void delete(int artistId) {

        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "DELETE FROM artist WHERE artist_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, Integer.toString(artistId));
            preparedStatement.execute();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Artist> findByCity(String city) {
        List<Artist> artists = new ArrayList<>();
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "SELECT * FROM artist where artist_city = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, city);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("artist_id");
                String name = resultSet.getString("artist_name");
                String bio = resultSet.getString("artist_bio");
                int birthYear = resultSet.getInt("artist_birthYear");
                String mail = resultSet.getString("artist_contactEmail");
                String phone = resultSet.getString("artist_phone");
                String website = resultSet.getString("artist_website");
                String socialMedia = resultSet.getString("artist_socialMedia");
                boolean active = resultSet.getBoolean("artist_isActive");
                artists.add(new Artist(id, name, bio, birthYear, mail, phone, city, website, socialMedia, active));
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return artists;
    }
}
