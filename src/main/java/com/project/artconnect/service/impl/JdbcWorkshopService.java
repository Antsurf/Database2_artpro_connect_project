package com.project.artconnect.service.impl;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Booking;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.persistence.JdbcWorkshopDao;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.ui.WorkshopController;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcWorkshopService implements WorkshopService {
    
    private final WorkshopDao workshopDao = new JdbcWorkshopDao();

    @Override
    public List<Workshop> getAllWorkshops()  {
        return workshopDao.findAll();
    }

    @Override
    public Optional<Workshop> getWorkshopByTitle(String title)  {
        return workshopDao.findAll().stream()
                .filter(w -> w.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    @Override
    public String getNumberOfBooking(Workshop workshop){
        int numberOfBookings = workshopDao.getNumberOfBooking(workshop);
        int numberMax = workshop.getMaxParticipants();
        return numberOfBookings + "/" + numberMax;
    }


    @Override
    public void bookWorkshop(Workshop workshop, CommunityMember member) {
        if (workshop == null || member == null) return;

        // Resolve workshop_id from the DB by title
        String sqlId = "SELECT workshop_id FROM Workshop WHERE workshop_title = ?";
        String sqlInsert =
                "INSERT INTO booking (workshop_id, cm_id, booking_bookingDate, booking_paymentStatus) " +
                        "VALUES (?, ?, ?, 'pending')";

        try (Connection conn = ConnectionManager.getConnection()) {

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

            Booking booking = new Booking(workshop, member);
            member.addBooking(booking);

        } catch (SQLException e) {
            System.out.println("bookWorkshop failed: " + e.getMessage());
        }
    }


    @Override
    public List<Booking> getBookingsByMember(CommunityMember member) {
        if (member == null) return List.of();

        // If already loaded, return directly
        if (!member.getBookings().isEmpty()) {
            return member.getBookings();
        }

        return List.of();
    }

    @Override
    public List<Workshop> getWorkshopsByMember(CommunityMember member){
        List<Booking> bookings = getBookingsByMember(member);
        List<Workshop> workshops = new ArrayList<>();
        for(Booking booking : bookings){
            workshops.add(booking.getWorkshop());
        }
        return workshops;
    }

    @Override
    public List<Workshop> filterByLevel(String level){
        return workshopDao.findAll().stream()
                .filter(e -> e.getLevel().equals(level))
                .toList();
    }
}