package com.project.artconnect.service;

import com.project.artconnect.model.Workshop;
import com.project.artconnect.model.Booking;
import com.project.artconnect.model.CommunityMember;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface WorkshopService {
    List<Workshop> getAllWorkshops() throws SQLException;

    Optional<Workshop> getWorkshopByTitle(String title) throws SQLException;

    void bookWorkshop(Workshop workshop, CommunityMember member);

    List<Booking> getBookingsByMember(CommunityMember member);
}
