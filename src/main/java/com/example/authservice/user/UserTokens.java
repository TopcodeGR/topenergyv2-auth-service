package com.example.authservice.user;

import java.util.Map;

public class UserTokens {

    private UserAccessToken accessToken;
    private UserRefreshToken refreshToken;
    private String clientId;

    public UserTokens(UserAccessToken accessToken, UserRefreshToken refreshToken, String clientId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.clientId = clientId;
    }


    public UserAccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(UserAccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public UserRefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(UserRefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
