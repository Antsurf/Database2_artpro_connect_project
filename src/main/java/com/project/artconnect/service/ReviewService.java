package com.project.artconnect.service;

import com.project.artconnect.model.Review;

public interface ReviewService {

    void createReview(Review review, int art_id, int cm_id);
}
