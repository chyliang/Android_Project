package com.example.fishcatcher.dto;

import com.example.fishcatcher.model.AppResponse;
import com.example.fishcatcher.dto.Catch;

public class ResultResponse extends AppResponse {

    private String catches;

    public void setResults(String r){
        catches = r;
    }

    public String getResults(){
        return catches;
    }
}
