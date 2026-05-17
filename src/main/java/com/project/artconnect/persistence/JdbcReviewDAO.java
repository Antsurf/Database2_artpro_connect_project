package com.project.artconnect.persistence;

import com.project.artconnect.dao.ReviewDao;
import com.project.artconnect.model.Review;
import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcReviewDAO implements ReviewDao {

    private Review mapReview(ResultSet rs) throws SQLException{
        Review review = new Review();
        review.setComment(rs.getString("review_comment"));
        review.setRating(rs.getBigDecimal("review_rating"));
        review.setType(rs.getString("review_type"));
        review.setReviewDate((LocalDate) rs.getObject("review_date"));
        return  review;
    }

    @Override
    public List<Review> findAllByCM(int cm_id) {
        List<Review> reviews = new ArrayList<>();
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "SELECT * FROM Review WHERE cm_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, cm_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reviews.add(mapReview(resultSet));
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return reviews;
    }


    @Override
    public void save(Review review) {
        try(Connection connection = ConnectionManager.getConnection()){
            String sql = "INSERT INTO review (artwork_id, cm_id, review_rating, review_comment, review_date, review_type) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, review.getArtwork().getId());
            preparedStatement.setInt(2, review.getReviewer().getId());
            preparedStatement.setBigDecimal(3, review.getRating());
            preparedStatement.setString(4,review.getComment());
            preparedStatement.setObject(5, review.getReviewDate());
            preparedStatement.setString(6,review.getType());

            preparedStatement.execute();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
