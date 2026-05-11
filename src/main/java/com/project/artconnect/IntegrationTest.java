package com.project.artconnect;

import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.model.*;
import com.project.artconnect.persistence.JdbcCommunityMemberDao;
import com.project.artconnect.service.*;
import com.project.artconnect.util.ServiceProvider;

import java.util.List;
import java.util.Optional;

/**
 * Integration test — runs through the full service layer using JDBC.
 * Each section tests READ, CREATE, UPDATE, DELETE via ServiceProvider.
 * If you see ✓ for every line, your DB integration is working correctly.
 */
public class IntegrationTest {

    static void section(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + title);
        System.out.println("=".repeat(60));
    }

    static void check(String label, boolean condition) {
        System.out.println("  " + (condition ? "✓" : "✗") + " " + label);
        if (!condition) System.out.println("    ^^^ FAILED");
    }

    static void info(String msg) {
        System.out.println("    → " + msg);
    }

    public static void main(String[] args) {

        ArtistService    artistService    = ServiceProvider.getArtistService();
        ArtworkService   artworkService   = ServiceProvider.getArtworkService();
        GalleryService   galleryService   = ServiceProvider.getGalleryService();
        WorkshopService  workshopService  = ServiceProvider.getWorkshopService();
        CommunityService communityService = ServiceProvider.getCommunityService();

        // ============================================================
        // 1. READ — verify data loads from DB
        // ============================================================
        section("1. READ — loading data from database");

        List<Artist> artists = artistService.getAllArtists();
        check("getAllArtists() returns data", !artists.isEmpty());
        artists.stream().limit(3).forEach(a -> info(a.getName() + " | " + a.getCity()));

        List<Artwork> artworks = artworkService.getAllArtworks();
        check("getAllArtworks() returns data", !artworks.isEmpty());
        artworks.stream().limit(3).forEach(a -> info(a.getTitle() + " | " + a.getStatus()
                + " | artist: " + (a.getArtist() != null ? a.getArtist().getName() : "none")));

        List<Gallery> galleries = galleryService.getAllGalleries();
        check("getAllGalleries() returns data", !galleries.isEmpty());
        galleries.stream().limit(3).forEach(g -> info(g.getName()
                + " | exhibitions: " + g.getExhibitions().size()));

        List<Workshop> workshops = workshopService.getAllWorkshops();
        check("getAllWorkshops() returns data", !workshops.isEmpty());
        workshops.stream().limit(3).forEach(w -> info(w.getTitle() + " | " + w.getLevel()
                + " | instructor: " + (w.getInstructor() != null ? w.getInstructor().getName() : "none")));

        List<CommunityMember> members = communityService.getAllMembers();
        check("getAllMembers() returns data", !members.isEmpty());
        members.stream().limit(3).forEach(m -> info(m.getName() + " | " + m.getEmail()));

        // ============================================================
        // 2. CREATE — insert a new artist and verify it appears
        // ============================================================
        section("2. CREATE — inserting a new artist");

        Artist newArtist = new Artist();
        newArtist.setName("TEST_INTEGRATION_ARTIST3");
        newArtist.setBio("Created by integration test");
        newArtist.setBirthYear(1990);
        newArtist.setContactEmail("test@integration.com");
        newArtist.setPhone("0600000000");
        newArtist.setCity("TestCity");
        newArtist.setWebsite("www.test.com");
        newArtist.setSocialMedia("@test");
        newArtist.setActive(true);
        System.out.println(newArtist);
        artistService.createArtist(newArtist);

        Optional<Artist> found = artistService.getArtistByName("TEST_INTEGRATION_ARTIST3");
        if (found.isPresent()) {
            System.out.println("Artist found!");

        } else {
            System.out.println("Artist not found!");
        }
        check("Artist found after createArtist()", found.isPresent());
        found.ifPresent(a -> info("Inserted: " + a.getName() + " | " + a.getCity()));

        // ============================================================
        // 3. UPDATE — change the city and verify it persisted
        // ============================================================
        section("3. UPDATE — changing artist city");

        found.ifPresent(a -> {
            a.setCity("UpdatedCity");
            artistService.updateArtist(a);
        });

        Optional<Artist> afterUpdate = artistService.getArtistByName("TEST_INTEGRATION_ARTIST3");
        check("City updated to 'UpdatedCity'",
                afterUpdate.isPresent() && "UpdatedCity".equals(afterUpdate.get().getCity()));
        afterUpdate.ifPresent(a -> info("After update: " + a.getName() + " | " + a.getCity()));

        // ============================================================
        // 4. DELETE — remove the test artist and verify it's gone
        // ============================================================
        section("4. DELETE — removing test artist");

        artistService.deleteArtist("TEST_INTEGRATION_ARTIST3");

        Optional<Artist> afterDelete = artistService.getArtistByName("TEST_INTEGRATION_ARTIST3");
        check("Artist gone after deleteArtist()", afterDelete.isEmpty());
        info(afterDelete.isEmpty() ? "Correctly deleted" : "ERROR: still present!");

        // ============================================================
        // 5. SEARCH — searchArtists filters correctly
        // ============================================================
        section("5. SEARCH — searchArtists()");

        if (!artists.isEmpty()) {
            String city = artists.get(0).getCity();
            List<Artist> byCity = artistService.searchArtists(null, null, city);
            check("searchArtists by city '" + city + "' returns results", !byCity.isEmpty());
            byCity.forEach(a -> info(a.getName() + " | " + a.getCity()));
        }

        // ============================================================
        // 6. DISCIPLINES
        // ============================================================
        section("6. DISCIPLINES");

        List<Discipline> disciplines = artistService.getAllDisciplines();
        check("getAllDisciplines() returns data", !disciplines.isEmpty());
        disciplines.forEach(d -> info(d.getName()));

        // ============================================================
        // 7. GALLERY — exhibitions loaded inside gallery object
        // ============================================================
        section("7. GALLERY — exhibitions");

        if (!galleries.isEmpty()) {
            Gallery firstGallery = galleries.get(1);
            List<?> exh = galleryService.getExhibitionsByGallery(firstGallery);
            check("getExhibitionsByGallery() returns list (can be empty)", exh != null);
            info(firstGallery.getName() + " has " + exh.size() + " exhibition(s)");
        }

        // ============================================================
        // 8. COMMUNITY — reviews by member
        // ============================================================
        section("8. COMMUNITY — reviews");

        if (!members.isEmpty()) {
            CommunityMemberDao communityMemberDao = new JdbcCommunityMemberDao();
            System.out.println(communityMemberDao.findById(1));
            CommunityMember firstMember = members.get(0);
            List<?> reviews = communityService.getReviewsByMember(firstMember);
            check("getReviewsByMember() returns list (can be empty)", reviews != null);
            info(firstMember.getName() + " has " + reviews.size() + " review(s)");
        }

        // ============================================================
        section("ALL TESTS DONE");
        System.out.println("  ✗ = something failed, check the line above it.");
        System.out.println("  ✓ = working correctly.\n");
    }
}