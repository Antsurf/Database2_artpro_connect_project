package com.project.artconnect.persistence;

import com.project.artconnect.dao.DisciplineDAO;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcDisciplineDAO implements DisciplineDAO {

    @Override
    public Discipline save(Discipline discipline) {
        boolean inDB = inDatabase(discipline);

        if(inDB){
            update(discipline);
        }
        else{
            discipline = insert(discipline);
        }

        return  discipline;
    }

    private boolean inDatabase(Discipline discipline){
        boolean inDB = false;
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "SELECT * FROM discipline WHERE discipline_id = ? or discipline_name =?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, Integer.toString(discipline.getId()));
            preparedStatement.setString(2, discipline.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                inDB = true;
            }
            return inDB;
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return inDB;
    }

    private Discipline insert(Discipline discipline){
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "INSERT INTO Discipline (discipline_name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, discipline.getName());
            preparedStatement.execute();

            sql = "SELECT discipline_id FROM discipline WHERE discipline_name = ?";
            preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setString(1, discipline.getName());
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                discipline.setId(resultSet.getInt("discipline_id"));
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return discipline;
    }

    private void update(Discipline discipline){
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "UPDATE Discipline SET discipline_name = ? WHERE discipline_id = ?";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setString(1, discipline.getName());
            preparedStatement.setString(2, Integer.toString(discipline.getId()));
            preparedStatement.execute();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveLink(int artistId, Discipline discipline) {

        if (!linkInDatabase(artistId, discipline)){
            discipline = save(discipline);

            try(Connection connection = ConnectionManager.getConnection()){
                String sql = "INSERT INTO is_specialized_in (artist_id, discipline_id) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, Integer.toString(artistId));
                preparedStatement.setString(2, Integer.toString(discipline.getId()));
                preparedStatement.execute();
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }

    }

    private boolean linkInDatabase(int artistId, Discipline discipline){
        boolean inDB = false;
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "SELECT * FROM is_specialized_in WHERE artist_id = ? AND discipline_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, Integer.toString(artistId));
            preparedStatement.setString(2, Integer.toString(discipline.getId()));
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                inDB = true;
            }
            return inDB;
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return inDB;

    }
}
