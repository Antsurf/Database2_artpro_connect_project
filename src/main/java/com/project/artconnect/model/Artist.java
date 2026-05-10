package com.project.artconnect.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Artist entity representing a creator in the community.
 */
public class Artist {
    private int id;
    private String name;
    private String bio;
    private int birthYear;
    private List<Discipline> disciplines = new ArrayList<>();
    private String contactEmail;
    private String phone;
    private String city;
    private String website;
    private String socialMedia;
    private boolean isActive;
    private List<Artwork> artworks = new ArrayList<>();

    public Artist() {
    }

    public Artist(int id, String name, String bio, int birthYear, String contactEmail, String phone, String city, String website, String socialMedia, boolean isActive) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.birthYear = birthYear;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.city = city;
        this.website = website;
        this.socialMedia = socialMedia;
        this.isActive = isActive;
    }

    public Artist(String name, String bio, int birthYear, String contactEmail, String city) {
        this.name = name;
        this.bio = bio;
        this.birthYear = birthYear;
        this.city = city;
        this.contactEmail = contactEmail;
    }

    // Getters and Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(String socialMedia) {
        this.socialMedia = socialMedia;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Artwork> getArtworks() {
        return artworks;
    }

    public void setArtworks(List<Artwork> artworks) {
        this.artworks = artworks;
    }


    public void addArtwork(Artwork artwork) {
        this.artworks.add(artwork);
        if (artwork.getArtist() != this) {
            artwork.setArtist(this);
        }
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bio='" + bio + '\'' +
                ", birthYear=" + birthYear +
                ", disciplines=" + disciplines +
                ", contactEmail='" + contactEmail + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", website='" + website + '\'' +
                ", socialMedia='" + socialMedia + '\'' +
                ", isActive=" + isActive +
                ", artworks=" + artworks +
                '}';
    }
}
