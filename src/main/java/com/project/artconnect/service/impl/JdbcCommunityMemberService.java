package com.project.artconnect.service.impl;

import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Review;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.persistence.JdbcCommunityMemberDao;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class JdbcCommunityMemberService implements CommunityService {

    private final CommunityMemberDao communityMemberDao = new JdbcCommunityMemberDao();

    @Override
    public List<CommunityMember> getAllMembers() {
        return communityMemberDao.findAll();
    }

    // returns all the people that have same name
    @Override
    public Optional<CommunityMember> getMemberByName(String name) {
        // same explanation as in JdbcArtworkService.java
        return communityMemberDao.findAll().stream().filter(m->m.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public CommunityMember getByEmail(String email){
        return communityMemberDao.findByEmail(email);
    }

    @Override
    public List<Review> getReviewsByMember(CommunityMember member) {
        if (member == null) return List.of();
        if (member.getReviews().isEmpty()) {
            List<Review> dbReviews = communityMemberDao.findReviewsByMemberId(member.getId());
            member.setReviews(dbReviews); // Update the object so we don't query again
        }

        return member.getReviews();
    }
}
