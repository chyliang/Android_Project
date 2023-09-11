package com.example.fishcatcher;

import com.example.fishcatcher.dto.Catch;

import java.io.Serializable;
import java.util.ArrayList;

public class Result implements Serializable {
    private ArrayList<Catch> results;

    public Result(){
        results = new ArrayList<>();
    }

    public void insertCatch(Catch c){
        results.add(c);
    }

    public ArrayList<Catch> getResults(){
        return results;
    }


}
