package com.example.authservice.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;
import java.util.Map;

@Document(collection = "users")
public class User {

    @Id
    private String _id;
    private String username;
    private String password;
    private String name;
    private String address;
    private String phone;
    private String VAT;
    private Number ceYear;
    private String logo;
    private Boolean active;
    private Map<String,UserAccessToken> accessTokens;
    private Map<String,UserRefreshToken> refreshTokens;
    private Date createdAt;
    private Date updatedAt;

    public User(String username, String password, String name, String address, String phone, String VAT, Number ceYear, String logo, Boolean active) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.VAT = VAT;
        this.ceYear = ceYear;
        this.logo = logo;
        this.active = active;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVAT() {
        return VAT;
    }

    public void setVAT(String VAT) {
        this.VAT = VAT;
    }

    public Number getCeYear() {
        return ceYear;
    }

    public void setCeYear(Number ceYear) {
        this.ceYear = ceYear;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Map<String, UserAccessToken> getAccessTokens() {
        return accessTokens;
    }

    public void setAccessTokens(Map<String, UserAccessToken> accessTokens) {
        this.accessTokens = accessTokens;
    }

    public Map<String, UserRefreshToken> getRefreshTokens() {
        return refreshTokens;
    }

    public void setRefreshTokens(Map<String, UserRefreshToken> refreshTokens) {
        this.refreshTokens = refreshTokens;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
