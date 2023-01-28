package com.topcode.topenergyv2.authservice.auth;

public class JWTTokenValidationData {

    private boolean isValid;
    private String message;

    public JWTTokenValidationData(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
