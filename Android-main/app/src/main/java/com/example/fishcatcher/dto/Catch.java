package com.example.fishcatcher.dto;

import java.util.Date;

/**
 * Catch is a row in the results returned from query
 **/
public class Catch {

    // contains all the column data
    private String userId;
    private double tempr;
    private double humid;
    private double baro;
    private double lat;
    private double lon;
    private String city;
    private String species;
    private double length;
    private double weight;
    private Date date;

    private String date1;


    public Catch() {
        this.userId = "";
        this.tempr = 0;
        this.humid = 0;
        this.baro = 0;
        this.lat = 0;
        this.lon = 0;
        this.city = "";
        this.species = "";
        this.length = 0;
        this.weight = 0;
        this.date = new Date("11/11/11");
    }

    public Catch(String userId, double temp, double humid, double baro, double lat, double lon,
                 String city, String species, double length, double weight, Date date){
        this.userId = userId;
        this.tempr = temp;
        this.humid = humid;
        this.baro = baro;
        this.lat = lat;
        this.lon = lon;
        this.city = city;
        this.species = species;
        this.length = length;
        this.weight = weight;
        this.date = date;

    }

    public String getUserId(){
        return userId;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getTemp() {
        return tempr;
    }

    public double getHumid() {
        return humid;
    }

    public double getBaro() {
        return baro;
    }

    public String getCity() {
        return city;
    }

    public String getSpecies() {
        return species;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return weight;
    }

    public Date getDate() {
        return date;
    }

    public String getDate1() {return date1; }

    public void setDate1(String date1) {this.date1 = date1; }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
