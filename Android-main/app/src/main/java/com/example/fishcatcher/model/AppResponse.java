package com.example.fishcatcher.model;

import com.example.fishcatcher.model.JsonModel;
public class AppResponse extends JsonModel {

    private String error;
    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
