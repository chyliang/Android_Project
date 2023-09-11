package com.example.fishcatcher;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fishcatcher.dto.SignUpResponse;
import com.example.fishcatcher.model.AppResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Catcher extends AppCompatActivity {

    /**  misc statics **/
    private static final int[] VALIDITY_CHECK_FIELDS = {R.id.length_tv,
                                                        R.id.species_tv,
                                                        R.id.weight_tv};

    /** AWS info **/
    private static final String url = "https://lvrakasbsb.execute-api.us-east-1.amazonaws.com/";

    /** Scraping info **/
    private static final String WEATHER_URL_BASE = "https://forecast.weather.gov/MapClick.php?";
    private final int LOCATION_PERMISSION_CODE = 1;
    private Document doc;

    /**
     * View info
     **/
    private Button catch_button;
    private TextView species_tv;
    private TextView weight_tv;
    private TextView length_tv;
    private TextView temp_tv;
    private TextView bar_tv;
    private TextView hum_tv;


    /**
     * Fish info
     **/
    private double fish_length;
    private double fish_weight;
    private String fish_species;

    /**
     * Env info
     **/
    private double barometric;
    private double temperature_f;
    private double humidity;

    /**
     * Loc info
     **/
    LocationListener locationListener;
    private double longitude;
    private double latitude;
    Location location;
    private String Lake_name;
    private String city;
    private String state;

    /**
     * Date info
     **/
    private Date date;

    /**
     * button info
     **/
    private Button findfishbutton;
    private Button logoutbutton;

    private void findAllViewsById() {
        catch_button = findViewById(R.id.catch_button);
        species_tv = findViewById(R.id.species_tv);
        weight_tv = findViewById(R.id.weight_tv);
        length_tv = findViewById(R.id.length_tv);
        temp_tv = findViewById(R.id.temp_tv);
        bar_tv = findViewById(R.id.bar_tv);
        hum_tv = findViewById(R.id.hum_tv);
        findfishbutton = findViewById(R.id.button_findfish);
        logoutbutton = findViewById(R.id.catch_logout);
//        mycatchesbutton = findViewById(R.id.button_mycatches);
    }

    private void setLocation(Location location){
        this.location = location;
    }

    /**
     * Uses phone to get its lat and lon
     **/
    private void getSet_lat_lon() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            return;
        }

        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }

    /**
     * Should halt the database insert if exception
     **/
    private double get_fish_length() {
        try {
            return Double.parseDouble(length_tv.getText().toString());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Should halt the database insert if exception
     **/
    private double get_fish_weight() {
        try {
            return Double.parseDouble(weight_tv.getText().toString());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return 0.0;
    }

    private String get_fish_species() {
        return species_tv.getText().toString();
    }

    private void set_temp_tv() {
        temp_tv.setText(String.valueOf(temperature_f));
    }

    private void set_hum_tv() {
        hum_tv.setText(String.valueOf(humidity));
    }

    private void set_bar_tv() {
        bar_tv.setText(String.valueOf(barometric));
    }

    private void set_fish_info() {
        fish_length = get_fish_length();
        fish_weight = get_fish_weight();
        fish_species = get_fish_species();
    }

    /**
     * uses doc to get barometric pressure
     **/
    private double getBarometric(Elements table_datas) {
        int index_of_barometric_value = 5;
        Pattern baro_pattern = Pattern.compile("^[0-9]*.[0-9]*");
        Matcher baro_matcher = baro_pattern.matcher(table_datas.get(index_of_barometric_value).text());
        if (baro_matcher.find()) {
            return Double.parseDouble(Objects.requireNonNull(baro_matcher.group(0)));
        }
//        System.out.println(barometric); //good

        return 0.0;
    }

    /**
     * uses doc to get temperature
     **/
    private double getTemperature_f(Elements table_datas_summary) {
        int index_of_temp_value = 0;
        Pattern temp_pattern = Pattern.compile("[0-9]*");
        Matcher temp_matcher = temp_pattern.matcher(table_datas_summary.get(index_of_temp_value).text());
        if (temp_matcher.find()) {
            return Double.parseDouble(Objects.requireNonNull(temp_matcher.group(0)));
        }
//        System.out.println(temperature_f); //good
        return 0.0;
    }

    /**
     * uses doc to get humidity
     **/
    private double getHumidity(Elements table_datas) {
        int index_of_humitidy_value = 1;
        Pattern humidity_pattern = Pattern.compile("^[0-9]*");
        Matcher humidity_matcher = humidity_pattern.matcher(table_datas.get(index_of_humitidy_value).text());
        if (humidity_matcher.find()) {
            return Double.parseDouble(Objects.requireNonNull(humidity_matcher.group(0)));
        }
//        System.out.println(humidity); //good

        return 0.0;
    }

    private void set_env_info() {
//        System.out.print("Is document null? ");
//        System.out.println(doc == null);
        assert doc != null;
        Element curr_cond_details = doc.getElementById("current_conditions_detail");
        assert curr_cond_details != null;
        Elements table_datas_env = curr_cond_details.getElementsByTag("td");
//        System.out.println(table_datas_env);

        Element curr_cond_summ = doc.getElementById("current_conditions-summary");
        assert curr_cond_summ != null;
        Elements table_datas_summary = curr_cond_summ.getElementsByClass("myforecast-current-lrg");
//        System.out.println(table_datas_summary);

        barometric = getBarometric(table_datas_env);
        humidity = getHumidity(table_datas_env);
        temperature_f = getTemperature_f(table_datas_summary);

        set_bar_tv();
        set_hum_tv();
        set_temp_tv();
    }

    private boolean isFishInfoFieldValid(int id){
        String value = findViewById(id).toString();
        value = value.replace(" ", "");
        return !value.equals("");
    }

    private boolean check_fish_info_inputs(){
        for (int validityCheckField : VALIDITY_CHECK_FIELDS) {
            if (!isFishInfoFieldValid(validityCheckField)) {
                return false;
            }
        }
        return true;
    }

    private void set_listeners() {

        /* CATCH BUTTON */
        catch_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(!check_fish_info_inputs()){
                    //toast message informing that the fish info must be completed.
                    Toast.makeText(getApplicationContext(), "after listeners",
                            Toast.LENGTH_LONG).show();
                    // TODO clear fish_info
                    species_tv.setText("");
                    length_tv.setText("");
                    weight_tv.setText("");
                    return;
                }
                getSet_document();
                set_env_info();
                set_fish_info();
                // TODO clear fish_info
                // upload to database
                species_tv.setText("");
                length_tv.setText("");
                weight_tv.setText("");

                /* put catch */
                Bundle bundle = getIntent().getExtras();
                String userId = bundle.getString("userId");

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();


                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject();
                    jsonObj.put("userId", userId);
                    jsonObj.put("tempr", temperature_f);
                    jsonObj.put("humid", humidity);
                    jsonObj.put("baro", barometric);
                    jsonObj.put("lon", longitude);
                    jsonObj.put("lat", latitude);
                    //jsonObj.put("city", city);
                    jsonObj.put("city", "Edison");
                    jsonObj.put("species", fish_species);
                    jsonObj.put("length1", fish_length);
                    jsonObj.put("weight", fish_weight);
                    jsonObj.put("species", fish_species);
                    //jsonObj.put("date", date);
                    jsonObj.put("date1", dtf.format(now));




                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.PUT, url, jsonObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppResponse response1 = AppResponse.newInstance(AppResponse.class, response.toString());
                        if (response1.getError().equals("0")) {
                            Intent intent = new Intent(Catcher.this, Catcher.class);
                            intent.putExtra("catch", response1.getMessage());
                            startActivity(intent);
                        }

                    }
                    }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error

                        }
                    });
                    RQueue.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                }

            }
        );

        /* LOCATION LISTENER */
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {
                setLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras){
//            if (locationListener != null) locationListener.onStatusChanged(provider, status, extras);
            }
        };

        /* FIND FISH BUTTON LISTENER */
        findfishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Catcher.this, FilterActivity.class);
                startActivity(intent);
            }
        });

//        /* MY CATCHES BUTTON LISTENER */
//        mycatchesbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(catcher.this, mycatches.class);
//                startActivity(intent);
//            }
//        });
//
    }

    private String build_weather_url_query_string() {
        return WEATHER_URL_BASE + "lat=" + latitude + "&lon=" + longitude;
    }

    private void getSet_document() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //dependency: have lat and lon set
                    String weatherURL_with_LatLon = build_weather_url_query_string();
                    doc = Jsoup.connect(weatherURL_with_LatLon).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        spin_till_load();

    }

    private void spin_till_load() {
        while (doc == null) {
            //spin till doc loads

            //TODO break if timeout start new thread with counter.
        }
        System.out.println("Document loaded");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catch_view);

        findAllViewsById();
        set_listeners();

        getSet_lat_lon();
        getSet_document();
        set_env_info();

//        checkLocationPermissions();

    }

    private void logout(){
        finishAffinity();
        startActivity(new Intent(this, Login.class));
    }

//    private void checkLocationPermissions(){
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            //Permission not granted to either
//            System.out.println("you DO NOT have permission to use the location");
//            requestFINELocationPermission();
//        } else {
//            //if ACCESS_FINE_LOCATION OR ACCESS_COARSE_LOCATION is permitted.
//            //i.e. permission has been granted
//            System.out.println("you have permission to use the location");
//        }
//    }

    private void requestFINELocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This is a message")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Catcher.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    //not necessary but good for debug
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

}