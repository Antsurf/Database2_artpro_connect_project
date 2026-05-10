package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.dao.DisciplineDAO;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Discipline;
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

    private List<Discipline> findDisciplineByArtistId(int id){
        List<Discipline> disciplines = new ArrayList<Discipline>();
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "SELECT * FROM is_specialized_in where artist_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, Integer.toString(id));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int disciplineId = resultSet.getInt("discipline_id");
                String sqlGetDiscipline = "SELECT discipline_name FROM Discipline WHERE discipline_id = ?";
                PreparedStatement preparedStatementGetDiscipline = connection.prepareStatement(sqlGetDiscipline);
                preparedStatementGetDiscipline.setString(1, Integer.toString(resultSet.getInt("discipline_id")));

                ResultSet resultSetDiscipline = preparedStatementGetDiscipline.executeQuery();

                if (resultSetDiscipline.next()){
                    disciplines.add(new Discipline(resultSet.getInt("discipline_id"), resultSetDiscipline.getString("discipline_name")));
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return disciplines;

    }

    private Artist mapArtist(ResultSet rs) throws SQLException {
        Artist artist = new Artist();
        artist.setId(rs.getInt("artist_id"));
        artist.setName(rs.getString("artist_name"));
        artist.setBio(rs.getString("artist_bio"));
        artist.setBirthYear(rs.getInt("artist_birthYear"));
        artist.setContactEmail(rs.getString("artist_contactEmail"));
        artist.setPhone(rs.getString("artist_phone"));
        artist.setCity(rs.getString("artist_city"));
        artist.setWebsite(rs.getString("artist_website"));
        artist.setSocialMedia(rs.getString("artist_socialMedia"));
        artist.setActive(rs.getBoolean("artist_isActive"));
        return artist;
    }


    @Override
    public List<Artist> findAll() {

        List<Artist> artists = new ArrayList<>();
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "SELECT * FROM artist";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Artist artist = mapArtist(resultSet);
                ArtworkDao jdbcArtworkDao = new JdbcArtworkDao();
                artist.setArtworks(jdbcArtworkDao.findByArtistId(artist.getId()));
                artist.setDisciplines(findDisciplineByArtistId(artist.getId()));
                artists.add(artist);
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
            if (resultSet.next()){
                Artist artist = mapArtist(resultSet);
                ArtworkDao jdbcArtworkDao = new JdbcArtworkDao();
                artist.setArtworks(jdbcArtworkDao.findByArtistId(artist.getId()));
                artist.setDisciplines(findDisciplineByArtistId(artist.getId()));
                return artist;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        Artist artist = null;
        return artist;
    }


    @Override
    public Artist findByName(String name){
        try(Connection connection = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM artist where artist_name = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet =preparedStatement.executeQuery();
            if (resultSet.next()){
                Artist artist = mapArtist(resultSet);
                ArtworkDao jdbcArtworkDao = new JdbcArtworkDao();
                artist.setArtworks(jdbcArtworkDao.findByArtistId(artist.getId()));
                artist.setDisciplines(findDisciplineByArtistId(artist.getId()));
                return artist;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    //TODO : create function check if in database
    //TODO : if artist in the database -> update, else, insert
    @Override
    public void save(Artist artist) {

        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "INSERT INTO Artist (artist_name, artist_bio," +
                    " artist_birthYear, artist_contactEmail, artist_phone, artist_city," +
                    " artist_website, artist_socialMedia, artist_isActive, artist_id) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            preparedStatement.setInt(10, artist.getId());
            preparedStatement.execute();

            ArtworkDao jdbcArtworkDao = new JdbcArtworkDao();
            for (Artwork artwork : artist.getArtworks()) {
                jdbcArtworkDao.save(artwork);
            }

            DisciplineDAO disciplineDAO = new JdbcDisciplineDAO();
            for (Discipline discipline : artist.getDisciplines()){
                disciplineDAO.saveLink(artist.getId(), discipline);
            }

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

            ArtworkDao artworkDao = new JdbcArtworkDao();
            for(Artwork artwork : artist.getArtworks()){
                if(!artworkDao.inDatabase(artwork)){
                    artworkDao.save(artwork);
                }
            }

            DisciplineDAO disciplineDAO = new JdbcDisciplineDAO();
            for (Discipline discipline : artist.getDisciplines()){
                disciplineDAO.saveLink(artist.getId(), discipline);
            }
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
                Artist artist = mapArtist(resultSet);
                ArtworkDao jdbcArtworkDao = new JdbcArtworkDao();
                artist.setArtworks(jdbcArtworkDao.findByArtistId(artist.getId()));
                artist.setDisciplines(findDisciplineByArtistId(artist.getId()));
                artists.add(artist);

            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return artists;
    }
}
