package com.project.artconnect.service.impl;

import com.project.artconnect.dao.ReviewDao;
import com.project.artconnect.model.Review;
import com.project.artconnect.persistence.JdbcReviewDAO;
import com.project.artconnect.service.ReviewService;

public class JdbcReviewService implements ReviewService {

    private final ReviewDao reviewDao = new JdbcReviewDAO();

    @Override
    public void createReview(Review review, int art_id, int cm_id){
        reviewDao.save(review, art_id, cm_id);
    }

}
