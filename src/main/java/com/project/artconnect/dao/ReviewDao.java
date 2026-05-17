package com.project.artconnect.dao;

import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Review;

import java.util.List;

public interface ReviewDao {
    List<Review> findAllByCM(int cm_id);

    void save(Review review, int artwork_id, int cm_id);
}
