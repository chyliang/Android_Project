package com.example.fishcatcher.dto;

import com.example.fishcatcher.model.AppResponse;

public class LoginResponse extends AppResponse {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
