package com.project.artconnect.dao;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Review;

import java.util.List;
import java.util.Optional;

public interface CommunityMemberDao {
    CommunityMember findById(Integer id);
    List<CommunityMember> findAll();
    CommunityMember findByEmail(String email);

    List<Review> findReviewsByMemberId(Integer id);

    void save(CommunityMember communityMember);
}
