package com.example.fishcatcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fishcatcher.dto.Catch;
import com.example.fishcatcher.dto.ResultResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FilterActivity extends AppCompatActivity {

    private static final boolean DEBUG = true;

    private static final String url = "https://pkvpfod83b.execute-api.us-east-1.amazonaws.com";

    private Button filterAndFind;
    private EditText min_length;
    private EditText max_length;
    private EditText min_weight;
    private EditText max_weight;
    private EditText min_temperature;
    private EditText max_temperature;
    private EditText min_barometric;
    private EditText max_barometric;
    private EditText min_humidity;
    private EditText max_humidity;
    private EditText min_date;
    private EditText max_date;
    private EditText city;
    private EditText species;
    private ImageButton filterbackbutton;



    private void findViewsById(){
        filterAndFind = findViewById(R.id.filterAndFind);
        min_length = findViewById(R.id.min_length);
        max_length = findViewById(R.id.max_length);
        min_weight = findViewById(R.id.min_weight);
        max_weight = findViewById(R.id.max_weight);
        min_temperature = findViewById(R.id.min_temperature);
        max_temperature = findViewById(R.id.max_temperature);
        min_barometric = findViewById(R.id.min_barometric);
        max_barometric = findViewById(R.id.max_barometric);
        min_humidity = findViewById(R.id.min_humidity);
        max_humidity = findViewById(R.id.max_humidity);
        min_date = findViewById(R.id.min_date);
        max_date = findViewById(R.id.max_date);
        city = findViewById(R.id.city);
        species = findViewById(R.id.species);
        filterbackbutton = findViewById(R.id.filter_backbutton);
    }

    private static boolean isAlpha(String s) {
        return s != null && s.matches("^[a-zA-Z]*$");
    }

    private boolean isDateValid(String str_date){
        DateFormat df = new SimpleDateFormat(min_date.getText().toString(), Locale.US);
        df.setLenient(false);
        try {
            df.parse(str_date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private void defaultAllFieldColors(){

    }

    private boolean isDoubleValid(String str_double){
        try{
            Double.parseDouble(str_double);
        } catch (NumberFormatException nfe){
            return false;
        }
        return true;
    }

    private boolean validateFields(){
        boolean isValid = true;

        // city  and species must be alpha characters only
        String cityText = city.getText().toString();
        if(!isAlpha(cityText)){
            // highlight city editText in red
            isValid = false;
        }
        String speciesText = species.getText().toString();
        if(!isAlpha(speciesText)){
            // highlight species editText in red
            isValid = false;
        }

        // date must be valid
        if(!isDateValid(min_date.getText().toString())){
            // highlight min_date editText in red
            isValid = false;
        }
        if(!isDateValid(max_date.getText().toString())){
            // highlight max_date editText in red
            isValid = false;
        }

        // todo all mins must be lower than max, respective

        // mins and maxes must be doubles
        if(!isDoubleValid(min_length.getText().toString())){
            // highlight min_length editText in red
            isValid = false;
        }
        if(!isDoubleValid(max_length.getText().toString())){
            // highlight max_length editText in red
            isValid = false;
        }
        if(!isDoubleValid(min_weight.getText().toString())){
            // highlight min_weight editText in red
            isValid = false;
        }
        if(!isDoubleValid(max_weight.getText().toString())){
            // highlight max_weight editText in red
            isValid = false;
        }
        if(!isDoubleValid(min_temperature.getText().toString())){
            // highlight min_temperature editText in red
            isValid = false;
        }
        if(!isDoubleValid(max_temperature.getText().toString())){
            // highlight max_temperature editText in red
            isValid = false;
        }
        if(!isDoubleValid(min_barometric.getText().toString())){
            // highlight min_barometric editText in red
            isValid = false;
        }
        if(!isDoubleValid(max_barometric.getText().toString())){
            // highlight max_barometric editText in red
            isValid = false;
        }
        if(!isDoubleValid(min_humidity.getText().toString())){
            // highlight min_humidity editText in red
            isValid = false;
        }
        if(!isDoubleValid(max_humidity.getText().toString())){
            // highlight max_humidity editText in red
            isValid = false;
        }


        return isValid;  // placeholder

    }

    private Result getCatchesFromJson(JSONObject jsonObj) throws JSONException {

        Result result = new Result();

        JSONArray json_catches = jsonObj.getJSONArray("catches");

        for (int i = 0; i < json_catches.length(); i++) {
            JSONObject jObj = json_catches.getJSONObject(i);

            String userId = jObj.getString("userId");
            double temp = Double.parseDouble(jObj.getString("temp"));
            double humid = Double.parseDouble(jObj.getString("humid"));
            double baro = Double.parseDouble(jObj.getString("baro"));
            double lat = Double.parseDouble(jObj.getString("lat"));
            double lon = Double.parseDouble(jObj.getString("lon"));
            String city = jObj.getString("city");
            String species = jObj.getString("species");
            double length = Double.parseDouble(jObj.getString("length"));
            double weight = Double.parseDouble(jObj.getString("weight"));
            Date date = new Date(jObj.getString("date"));

            Catch c1 = new Catch(userId,temp,humid,baro,lat,lon,city,species,length,weight,date);

            result.insertCatch(c1);
        }
        return result;
    }

    private void set_listeners(){

        /* FILTER AND FIND BUTTON */
        filterAndFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateFields()){
                    Toast.makeText(getApplicationContext(), "One or more fields are invalid", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject();
                    //TODO check each one for empty string and put null if empty.
                    jsonObject.put("species", species.getText().toString());
                    jsonObject.put("min_length", min_length.getText().toString());
                    jsonObject.put("max_length", max_length.getText().toString());
                    jsonObject.put("min_weight", min_weight.getText().toString());
                    jsonObject.put("max_weight", max_weight.getText().toString());
                    jsonObject.put("min_temp", min_temperature.getText().toString());
                    jsonObject.put("max_temp", max_temperature.getText().toString());
                    jsonObject.put("min_baro", min_barometric.getText().toString());
                    jsonObject.put("max_baro", max_barometric.getText().toString());
                    jsonObject.put("min_humid", min_humidity.getText().toString());
                    jsonObject.put("max_humid", max_humidity.getText().toString());
                    jsonObject.put("min_date", min_date.getText().toString());
                    jsonObject.put("max_date", max_date.getText().toString());
                    jsonObject.put("city", city.getText().toString());

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                    // return?
                };

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                if(DEBUG){
                                    // insert test data
                                    Intent intent = new Intent(FilterActivity.this, MyCatches.class);
                                    Catch catch1 = new Catch();
                                    Catch catch2 = new Catch();
                                    Result resultObj = new Result();
                                    // put the results into arraylist
                                    resultObj.insertCatch(catch1);
                                    resultObj.insertCatch(catch2);
                                    intent.putExtra("result", resultObj);
                                    return;
                                }

                                // cam I just putExtra response?  no JSONObject isn't serializable.  (percelable is the better way anyway)
                                ResultResponse response1 = ResultResponse.newInstance(ResultResponse.class, response.toString());
                                if (response1.getError().equals("0")) {
                                    Intent intent = new Intent(FilterActivity.this, MyCatches.class);
                                    Gson gson = new Gson();
                                    Catch catches[] = gson.fromJson(response1.getResults(), Catch[].class);
                                    Result resultObj = new Result();
                                    for(Catch c : catches){
                                        resultObj.insertCatch(c);
                                    }
                                    intent.putExtra("result", resultObj);
                                    startActivity(intent);
                                }
                                else if(response1.getError().equals("1")){
                                    //no fish under that name
                                }
                                else if(response1.getError().equals("2")){
                                    //invalid request (all filter fields are empty)
                                }
                                else  {

                                    // shouldn't fail if 0 records returned. !!!
                                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                    /*
                                    display popup when login fails
                                        maybe use my message i put when error, should be saved under AppResponse response1
                                     */
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error

                            }
                        });
                RQueue.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

            }
        });

        filterbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_view);

        findViewsById();
        set_listeners();

    }
}