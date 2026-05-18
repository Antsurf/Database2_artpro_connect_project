package com.project.artconnect;

import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.model.*;
import com.project.artconnect.persistence.JdbcCommunityMemberDao;
import com.project.artconnect.service.*;
import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.util.ServiceProvider;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Integration test — runs through the full service layer using JDBC.
 * Each section tests READ, CREATE, UPDATE, DELETE via ServiceProvider.
 */
public class IntegrationTest {
    public static void main(String[] args) {

        ArtistService    artistService    = ServiceProvider.getArtistService();
        ArtworkService   artworkService   = ServiceProvider.getArtworkService();
        GalleryService   galleryService   = ServiceProvider.getGalleryService();
        WorkshopService  workshopService  = ServiceProvider.getWorkshopService();
        CommunityService communityService = ServiceProvider.getCommunityService();

        System.out.println("\n1. READ");

        List<Artist> artists = artistService.getAllArtists();
        System.out.println("getAllArtists(): " + !artists.isEmpty());
        artists.stream().limit(3).forEach(a -> System.out.println(a.getName() + " | " + a.getCity()));

        List<Artwork> artworks = artworkService.getAllArtworks();
        System.out.println("getAllArtworks(): " + !artworks.isEmpty());
        artworks.stream().limit(3).forEach(a -> System.out.println(a.getTitle() + " | " + a.getStatus() + " | artist: " + (a.getArtist() != null ? a.getArtist().getName() : "none")));

        List<Gallery> galleries = galleryService.getAllGalleries();
        System.out.println("getAllGalleries(): " + !galleries.isEmpty());
        galleries.stream().limit(3).forEach(g -> System.out.println(g.getName() + " | exhibitions: " + g.getExhibitions().size()));

        List<Workshop> workshops = workshopService.getAllWorkshops();
        System.out.println("getAllWorkshops(): " + !workshops.isEmpty());
        workshops.stream().limit(3).forEach(w -> System.out.println(w.getTitle() + " | " + w.getLevel() + " | instructor: " + (w.getInstructor() != null ? w.getInstructor().getName() : "none")));

        List<CommunityMember> members = communityService.getAllMembers();
        System.out.println("getAllMembers(): " + !members.isEmpty());
        members.stream().limit(3).forEach(m -> System.out.println(m.getName() + " | " + m.getEmail()));

        System.out.println("\n2. CREATE");

        Artist newArtist = new Artist();
        newArtist.setName("TEST_INTEGRATION_ARTIST4");
        newArtist.setBio("Created by integration test");
        newArtist.setBirthYear(1990);
        newArtist.setContactEmail("test@integration.com");
        newArtist.setPhone("0600000000");
        newArtist.setCity("TestCity");
        newArtist.setWebsite("www.test.com");
        newArtist.setSocialMedia("@test");
        newArtist.setActive(true);
        artistService.createArtist(newArtist);

        Optional<Artist> found = artistService.getArtistByName("TEST_INTEGRATION_ARTIST4");
        System.out.println("Artist found after create: " + found.isPresent());
        found.ifPresent(a -> System.out.println("Inserted: " + a.getName() + " | " + a.getCity()));

        System.out.println("\n3. UPDATE");

        found.ifPresent(a -> { a.setCity("UpdatedCity"); artistService.updateArtist(a); });
        Optional<Artist> afterUpdate = artistService.getArtistByName("TEST_INTEGRATION_ARTIST4");
        System.out.println("City updated: " + (afterUpdate.isPresent() && "UpdatedCity".equals(afterUpdate.get().getCity())));
        afterUpdate.ifPresent(a -> System.out.println("After update: " + a.getName() + " | " + a.getCity()));

        System.out.println("\n4. DELETE");

        artistService.deleteArtist("TEST_INTEGRATION_ARTIST4");
        Optional<Artist> afterDelete = artistService.getArtistByName("TEST_INTEGRATION_ARTIST4");
        System.out.println("Artist gone after delete: " + afterDelete.isEmpty());

        System.out.println("\n5. SEARCH");

        if (!artists.isEmpty()) {
            String city = artists.get(0).getCity();
            System.out.println(city);

            if (city != null && !city.isEmpty()) {
                List<Artist> byCity = artistService.searchArtists(null, null, city);
                System.out.println("searchArtists by city '" + city + "': " + !byCity.isEmpty());
                byCity.forEach(a -> System.out.println(a.getName() + " | " + a.getCity()));
            } else {
                System.out.println("Skipped - first artist has no city set");
            }
        }

        System.out.println("\n6. DISCIPLINES");

        List<Discipline> disciplines = artistService.getAllDisciplines();
        System.out.println("getAllDisciplines(): " + !disciplines.isEmpty());
        disciplines.forEach(d -> System.out.println(d.getName()));

        System.out.println("\n7. GALLERY");

        if (!galleries.isEmpty()) {
            Gallery firstGallery = galleries.get(1);
            List<?> exh = galleryService.getExhibitionsByGallery(firstGallery);
            System.out.println("getExhibitionsByGallery() not null: " + (exh != null));
            System.out.println(firstGallery.getName() + " has " + exh.size() + " exhibition(s)");
        }

        System.out.println("\n8. COMMUNITY");

        CommunityMemberDao communityMemberDao = new JdbcCommunityMemberDao();
        CommunityMember alice = communityMemberDao.findById(1);
        List<Review> reviews = communityService.getReviewsByMember(alice);
        System.out.println("getReviewsByMember() not null: " + (reviews != null));
        System.out.println(alice.getName() + " has " + reviews.size() + " review(s)");
        reviews.forEach(r -> System.out.println(r.getComment() + " | " + r.getRating() + " | " + r.getReviewDate()));

        System.out.println("\n9. TRANSACTION — register to all future workshops with beginner level");

        try (Connection connection = ConnectionManager.getConnection()) {
            CallableStatement cs = connection.prepareCall("CALL learn_painting(?)");
            cs.setInt(1, 1); // Alice cm_id = 1
            cs.execute();
            System.out.println("learn_painting(1) executed successfully");

            // verify bookings were created
            CommunityMemberDao communityMemberDao2 = new JdbcCommunityMemberDao();
            CommunityMember alice2 = communityMemberDao2.findById(1);
            System.out.println("Alice now has " + alice2.getBookings().size() + " booking(s)");
            alice2.getBookings().forEach(b -> System.out.println(
                    b.getWorkshop().getTitle() + " | " + b.getPaymentStatus()
            ));
        } catch (SQLException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }
        System.out.println("\nDONE");
    }
}