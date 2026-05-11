package com.project.artconnect.service.impl;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Booking;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.persistence.JdbcWorkshopDao;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcWorkshopService implements WorkshopService {
    
    // TODO: check if working after merge
    private final WorkshopDao workshopDao = new JdbcWorkshopDao();

    // ----------------------------------------------------------------
    // getAllWorkshops
    // ----------------------------------------------------------------
    @Override
    public List<Workshop> getAllWorkshops()  {
        return workshopDao.findAll();
    }

    // ----------------------------------------------------------------
    // getWorkshopByTitle
    // ----------------------------------------------------------------
    @Override
    public Optional<Workshop> getWorkshopByTitle(String title)  {
        return workshopDao.findAll().stream()
                .filter(w -> w.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    // ----------------------------------------------------------------
    // bookWorkshop
    // Inserts a booking row into the 'booking' table.
    // Uses a transaction: if the insert fails, nothing is committed.
    // ----------------------------------------------------------------
    @Override
    public void bookWorkshop(Workshop workshop, CommunityMember member) {
        if (workshop == null || member == null) return;

        // Resolve workshop_id from the DB by title
        String sqlId = "SELECT workshop_id FROM Workshop WHERE workshop_title = ?";
        String sqlInsert =
                "INSERT INTO booking (workshop_id, cm_id, booking_bookingDate, booking_paymentStatus) " +
                        "VALUES (?, ?, ?, 'pending')";

        try (Connection conn = ConnectionManager.getConnection()) {
            conn.setAutoCommit(false); // begin transaction

            int workshopId = -1;
            try (PreparedStatement ps = conn.prepareStatement(sqlId)) {
                ps.setString(1, workshop.getTitle());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) workshopId = rs.getInt("workshop_id");
                }
            }

            if (workshopId == -1) {
                System.out.println("bookWorkshop: workshop not found in DB — " + workshop.getTitle());
                conn.rollback();
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setInt(1, workshopId);
                ps.setInt(2, member.getId());
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                ps.executeUpdate();
            }

            conn.commit(); // all good

            // Also update the in-memory member object so the UI stays consistent
            Booking booking = new Booking(workshop, member);
            member.addBooking(booking);

        } catch (SQLException e) {
            System.out.println("bookWorkshop failed: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------
    // getBookingsByMember
    // Returns the member's bookings — already loaded if findById was
    // called, otherwise fetched directly from DB.
    // ----------------------------------------------------------------
    @Override
    public List<Booking> getBookingsByMember(CommunityMember member) {
        if (member == null) return List.of();

        // If already loaded, return directly
        if (!member.getBookings().isEmpty()) {
            return member.getBookings();
        }

        return List.of();
    }
}