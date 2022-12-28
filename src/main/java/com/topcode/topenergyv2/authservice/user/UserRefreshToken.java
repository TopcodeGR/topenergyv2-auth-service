package com.topcode.topenergyv2.authservice.user;

import java.util.Date;

public class UserRefreshToken {
    private String token;
    private Date expiresOn;

    public UserRefreshToken(String token, Date expiresOn) {
        this.token = token;
        this.expiresOn = expiresOn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }
}
