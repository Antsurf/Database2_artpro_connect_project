package com.project.artconnect.persistence;
import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.model.*;
import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCommunityMemberDao implements CommunityMemberDao{

    @Override
    public CommunityMember findById(Integer id) {
        CommunityMember cm = new CommunityMember();
        try (Connection connection = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM communitymember WHERE cm_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                cm.setId(resultSet.getInt("cm_id"));
                cm.setName(resultSet.getString("cm_name"));
                cm.setEmail(resultSet.getString("cm_email"));
                cm.setBirthYear(resultSet.getInt("cm_birthYear"));
                cm.setPhone(resultSet.getString("cm_phone"));
                cm.setCity(resultSet.getString("cm_city"));
                cm.setMembershipType(resultSet.getString("cm_membershipType"));
            }
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
        try (Connection connection = ConnectionManager.getConnection()) {
            String sql = "SELECT d.discipline_name FROM communitymember AS cm JOIN favorite AS f ON cm.cm_id = f.cm_id JOIN discipline AS d ON f.discipline_id = d.discipline_id WHERE cm.cm_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Discipline discipline = new Discipline(resultSet.getString("discipline_name"));
                cm.addFavoriteDiscipline(discipline);
            }
        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }
        try (Connection connection = ConnectionManager.getConnection()) {
            String sql = "SELECT w.workshop_id, b.booking_bookingDate, b.booking_paymentStatus FROM booking AS b JOIN communitymember AS cm ON b.cm_id = cm.cm_id JOIN workshop AS w ON b.workshop_id = w.workshop_id WHERE cm.cm_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Booking booking = new Booking();
                JdbcWorkshopDao w = new JdbcWorkshopDao();
                booking.setWorkshop(w.findById(resultSet.getInt("workshop_id")));
                booking.setMember(cm);
                booking.setBookingDate(resultSet.getObject("booking_bookingDate", LocalDateTime.class));
                booking.setPaymentStatus(resultSet.getString("booking_paymentStatus"));
                cm.addBooking(booking);
            }
        }
        catch(SQLException s){
            System.out.println(s.getMessage());
        }
        try (Connection connection = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM review WHERE cm_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Review r = new Review();
                JdbcArtworkDao a = new JdbcArtworkDao();
                r.setArtwork(a.findById(resultSet.getInt("artwork_id")));
                r.setReviewer(cm);
                r.setRating(resultSet.getBigDecimal("review_rating"));
                r.setComment(resultSet.getString("review_comment"));
                r.setReviewDate(resultSet.getObject("review_date", LocalDate.class));
                cm.addReview(r);
            }
        }
        catch(SQLException s){
            System.out.println(s.getMessage());
        }
        return cm;
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

    @Override
    public List<Review> findReviewsByMemberId(Integer id){
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM review r JOIN Artworks a ON r.artwork_id = a.artwork_id WHERE r.cm_id = ?";

        try (Connection conn = ConnectionManager.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Review review = new Review();
                review.setComment(rs.getString("review_comment"));
                review.setRating(rs.getBigDecimal("review_rating"));
                review.setReviewDate(rs.getDate("review_date").toLocalDate());

                reviews.add(review);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reviews;
    }

    @Override
    public void save(CommunityMember communityMember){
        try(Connection connection = ConnectionManager.getConnection()){
            String sql ="INSERT INTO CommunityMember (cm_name, cm_email, cm_birthYear, cm_phone, cm_city, cm_membershipType) VALUES (?, ?, ?, ?, ?,'Standard')";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, communityMember.getName());
            preparedStatement.setString(2, communityMember.getEmail());
            preparedStatement.setInt(3, communityMember.getBirthYear());
            preparedStatement.setString(4, communityMember.getPhone());
            preparedStatement.setString(5, communityMember.getCity());

            preparedStatement.execute();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
