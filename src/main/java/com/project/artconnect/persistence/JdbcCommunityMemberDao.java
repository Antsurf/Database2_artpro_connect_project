package com.project.artconnect.persistence;
import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCommunityMemberDao implements CommunityMemberDao{

    @Override
    public Optional<CommunityMember> findById(Integer id) {
        CommunityMember cm = new CommunityMember();
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "SELECT * FROM communitymember WHERE cm_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            cm.setId(resultSet.getInt("cm_id"));
            cm.setName(resultSet.getString("cm_name"));
            cm.setEmail(resultSet.getString("cm_email"));
            cm.setBirthYear(resultSet.getInt("cm_birthYear"));
            cm.setPhone(resultSet.getString("cm_phone"));
            cm.setCity(resultSet.getString("cm_city"));
            cm.setMembershipType(resultSet.getString("cm_membershipType"));
        }
        catch(SQLException s){
            System.out.println(s.getMessage());
        }
        return Optional.of(cm);
    }

    @Override
    public List<CommunityMember> findAll() {
        List<CommunityMember> lst_member = new ArrayList<>();
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "SELECT * FROM communitymember";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                CommunityMember cm = new CommunityMember();
                cm.setId(resultSet.getInt("cm_id"));
                cm.setName(resultSet.getString("cm_name"));
                cm.setEmail(resultSet.getString("cm_email"));
                cm.setBirthYear(resultSet.getInt("cm_birthYear"));
                cm.setPhone(resultSet.getString("cm_phone"));
                cm.setCity(resultSet.getString("cm_city"));
                cm.setMembershipType(resultSet.getString("cm_membershipType"));
                lst_member.add(cm);
            }
        }
        catch(SQLException s){
            System.out.println(s.getMessage());
        }
        return lst_member;
    }
}
