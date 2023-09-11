package com.example.fishcatcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fishcatcher.dto.SignUpResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {

    private ImageButton signUpButton2,signupbackbutton;
    private EditText newUserId;
    private EditText newPassword;
    private static String url = "https://uak4qzupf7.execute-api.us-east-1.amazonaws.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_view);

        newUserId = (EditText)findViewById(R.id.editTextTextPersonName);
        newPassword = (EditText)findViewById(R.id.editTextTextPersonName2);
        signUpButton2 = findViewById(R.id.login);
        signUpButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject();
                    jsonObj.put("userId", newUserId.getText().toString());
                    jsonObj.put("password", newPassword.getText().toString());

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.PUT, url, jsonObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SignUpResponse response1 = SignUpResponse.newInstance(SignUpResponse.class,  response.toString());
                        if(response1.getError().equals("0")) {
                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                            //if sign up successful, give some response
                            Toast.makeText(getApplicationContext(),"Sign up successfully!",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            /*
                            display popup when username exists
                            maybe use my message i put when error, should be saved under AppResponse response1
                            */
                            //notice the user and clear the password area
                            Toast.makeText(getApplicationContext(),"Username already exists.",Toast.LENGTH_SHORT).show();
                            newPassword.getText().clear();
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
        });
        signupbackbutton = findViewById(R.id.signup_backbutton);
        signupbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}