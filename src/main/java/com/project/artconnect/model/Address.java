package com.project.artconnect.model;

public class Address {
    private int id;
    private int street_number;
    private int postal_code;
    private String street_name;
    private String city_name;
    private String country_name;

    public Address(){
    }

    public Address(int id, int street_number, int postal_code, String street_name, String city_name, String country_name) {
        this.id = id;
        this.street_number = street_number;
        this.postal_code = postal_code;
        this.street_name = street_name;
        this.city_name = city_name;
        this.country_name = country_name;
    }


    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getStreet_number() {return street_number;}
    public void setStreet_number(int street_number) {this.street_number = street_number;}
    public int getPostal_code() {return postal_code;}
    public void setPostal_code(int postal_code) {this.postal_code = postal_code;}
    public String getCity_name() {return city_name;}
    public void setCity_name(String city_name) {this.city_name = city_name;}
    public String getStreet_name() {return street_name;}
    public void setStreet_name(String street_name) {this.street_name = street_name;}
    public String getCountry_name() {return country_name;}
    public void setCountry_name(String country_name) {this.country_name = country_name;}

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street_number=" + street_number +
                ", postal_code=" + postal_code +
                ", street_name='" + street_name + '\'' +
                ", city_name='" + city_name + '\'' +
                ", country_name='" + country_name + '\'' +
                '}';
    }
}
