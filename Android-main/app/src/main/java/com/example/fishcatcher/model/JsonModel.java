package com.example.fishcatcher.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class JsonModel<T> {

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public static <T extends JsonModel<?>> T newInstance(Class<T> klass, String json) {
        Gson gson = new Gson();
        return (T) gson.fromJson(json, klass);
    }
}
