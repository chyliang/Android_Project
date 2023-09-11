package com.example.fishcatcher.model;

import java.util.Date;

public class Filter {

    private String species;
    private double min_length;
    private double max_length;
    private double min_weight;
    private double max_weight;
    private double min_temperature;
    private double max_temperature;
    private double min_barometric;
    private double max_barometric;
    private double min_humidity;
    private double max_humidity;
    private Date min_date;
    private Date max_date;
    private String city;

    public Filter(String species, double min_length, double max_length, double min_weight,
                  double max_weight, double min_temperature, double max_temperature,
                  double min_barometric, double max_barometric, double min_humidity,
                  double max_humidity, Date min_date, Date max_date, String city) {
        this.species = species;
        this.min_length = min_length;
        this.max_length = max_length;
        this.min_weight = min_weight;
        this.max_weight = max_weight;
        this.min_temperature = min_temperature;
        this.max_temperature = max_temperature;
        this.min_barometric = min_barometric;
        this.max_barometric = max_barometric;
        this.min_humidity = min_humidity;
        this.max_humidity = max_humidity;
        this.min_date = min_date;
        this.max_date = max_date;
        this.city = city;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public double getMin_length() {
        return min_length;
    }

    public void setMin_length(double min_length) {
        this.min_length = min_length;
    }

    public double getMax_length() {
        return max_length;
    }

    public void setMax_length(double max_length) {
        this.max_length = max_length;
    }

    public double getMin_weight() {
        return min_weight;
    }

    public void setMin_weight(double min_weight) {
        this.min_weight = min_weight;
    }

    public double getMax_weight() {
        return max_weight;
    }

    public void setMax_weight(double max_weight) {
        this.max_weight = max_weight;
    }

    public double getMin_temperature() {
        return min_temperature;
    }

    public void setMin_temperature(double min_temperature) {
        this.min_temperature = min_temperature;
    }

    public double getMax_temperature() {
        return max_temperature;
    }

    public void setMax_temperature(double max_temperature) {
        this.max_temperature = max_temperature;
    }

    public double getMin_barometric() {
        return min_barometric;
    }

    public void setMin_barometric(double min_barometric) {
        this.min_barometric = min_barometric;
    }

    public double getMax_barometric() {
        return max_barometric;
    }

    public void setMax_barometric(double max_barometric) {
        this.max_barometric = max_barometric;
    }

    public double getMin_humidity() {
        return min_humidity;
    }

    public void setMin_humidity(double min_humidity) {
        this.min_humidity = min_humidity;
    }

    public double getMax_humidity() {
        return max_humidity;
    }

    public void setMax_humidity(double max_humidity) {
        this.max_humidity = max_humidity;
    }

    public Date getMin_date() {
        return min_date;
    }

    public void setMin_date(Date min_date) {
        this.min_date = min_date;
    }

    public Date getMax_date() {
        return max_date;
    }

    public void setMax_date(Date max_date) {
        this.max_date = max_date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
