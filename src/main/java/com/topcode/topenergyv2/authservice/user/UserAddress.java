package com.topcode.topenergyv2.authservice.user;

public class UserAddress {

    private String street;
    private String state;
    private String city;
    private String zipCode;
    private String country;

    public UserAddress(String street, String state, String city, String zipCode, String country) {
        this.street = street;
        this.state = state;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
    }

    public UserAddress() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
