package com.example.authservice.exceptions;

public class GlobalException extends RuntimeException{

    private String apiMessage;
    public GlobalException(String message, String apiMessage) {
        super(message);
        this.apiMessage = apiMessage;
    }

    public String getApiMessage() {
        return apiMessage;
    }

    public void setApiMessage(String apiMessage) {
        this.apiMessage = apiMessage;
    }
}
