package com.project.artconnect.model;

import java.util.ArrayList;
import java.util.List;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Exhibition;

/**
 * Artwork entity representing a piece created by an artist.
 */
public class Artwork {
    private Integer id;
    private String title;
    private Integer creationYear;
    private String type; // painting, sculpture, etc.
    private String medium; // oil, watercolor, etc.
    private String dimensions;
    private String description;
    private double price;
    private Status status; // FOR_SALE, SOLD, EXHIBITED
    private Artist artist;
    private List<ArtworkTag> tags = new ArrayList<>();
    private Exhibition exhibition;
    public enum Status {
        FOR_SALE, SOLD, EXHIBITED
    }

    public Artwork() {
    }

    public Artwork(String title, Integer creationYear, String type, double price, Artist artist, Exhibition exhibition) {
        this.title = title;
        this.creationYear = creationYear;
        this.type = type;
        this.price = price;
        this.artist = artist;
        this.status = Status.FOR_SALE;
        this.exhibition = exhibition;
    }

    // Getters and Setters


    public Exhibition getExhibition() {return exhibition;}

    public void setExhibition(Exhibition exhibition) {this.exhibition = exhibition;}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCreationYear() {
        return creationYear;
    }

    public void setCreationYear(Integer creationYear) {
        this.creationYear = creationYear;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public List<ArtworkTag> getTags() {
        return tags;
    }

    public void setTags(List<ArtworkTag> tags) {
        this.tags = tags;
    }
    @Override
    public String toString() {
        return title;
    }
}
