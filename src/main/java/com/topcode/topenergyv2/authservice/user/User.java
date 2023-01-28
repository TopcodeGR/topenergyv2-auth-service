package com.topcode.topenergyv2.authservice.user;

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
    private String firstName;
    private String lastName;
    private String bussinessSector;
    private String bussinessName;
    private UserAddress address;
    private String phone;
    private String email;
    private String VAT;
    private String DOI;
    private Number ceYear;
    private String stripeCustomerId;

    private String elorusCustomerId;
    private String logo;
    private Boolean active;
    private Map<String,UserAccessToken> accessTokens;
    private Map<String,UserRefreshToken> refreshTokens;
    private Date createdAt;
    private Date updatedAt;



    public User(String _id, String username, String password, String firstName, String lastName, String bussinessSector, String bussinessName, UserAddress address, String phone,String email,  String VAT, String DOI, Number ceYear, String stripeCustomerId, String elorusCustomerId, String logo, Boolean active) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bussinessSector = bussinessSector;
        this.bussinessName = bussinessName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.VAT = VAT;
        this.DOI = DOI;
        this.ceYear = ceYear;
        this.stripeCustomerId = stripeCustomerId;
        this.elorusCustomerId = elorusCustomerId;
        this.logo = logo;
        this.active = active;
    }

    public User() {
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


    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
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

    public void updateOrInsertAccessToken(UserAccessToken accessToken, String clientId){
        this.accessTokens.put(clientId,accessToken);
    }

    public void updateOrInsertRefreshToken(UserRefreshToken refreshToken, String clientId){
        this.refreshTokens.put(clientId,refreshToken);
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

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getElorusCustomerId() {
        return elorusCustomerId;
    }

    public void setElorusCustomerId(String elorusCustomerId) {
        this.elorusCustomerId = elorusCustomerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBussinessSector() {
        return bussinessSector;
    }

    public void setBussinessSector(String bussinessSector) {
        this.bussinessSector = bussinessSector;
    }

    public String getBussinessName() {
        return bussinessName;
    }

    public void setBussinessName(String bussinessName) {
        this.bussinessName = bussinessName;
    }

    public String getDOI() {
        return DOI;
    }

    public void setDOI(String DOI) {
        this.DOI = DOI;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
