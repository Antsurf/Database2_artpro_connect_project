package com.project.artconnect.util;

import com.project.artconnect.service.*;
import com.project.artconnect.service.impl.*;

/**
 * Service Provider to manage singleton instances of services.
 * Uses JDBC implementations connected to the ArtConnect MySQL database.

 */
public class ServiceProvider {

    private static final ArtistService    artistService    = new JdbcArtistService();
    private static final ArtworkService   artworkService   = new JdbcArtworkService();
    private static final GalleryService   galleryService   = new JdbcGalleryService();
    private static final WorkshopService  workshopService  = new JdbcWorkshopService();
    private static final CommunityService communityService = new JdbcCommunityMemberService();
    private static final ExhibitionService exhibitionService = new JdbcExhibitionService();

    // No static init block needed

    public static ArtistService getArtistService() {
        return artistService;
    }

    public static ArtworkService getArtworkService() {
        return artworkService;
    }

    public static GalleryService getGalleryService() {
        return galleryService;
    }

    public static WorkshopService getWorkshopService() {
        return workshopService;
    }

    public static CommunityService getCommunityService() {
        return communityService;
    }

    public static ExhibitionService getExhibitionService(){ return exhibitionService;}
}