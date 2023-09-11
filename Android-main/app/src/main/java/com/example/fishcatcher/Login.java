package com.example.fishcatcher;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.fishcatcher.dto.LoginResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Field;
import java.util.Objects;

public class Login extends AppCompatActivity {

    private ImageButton loginbutton;
    private Button signupbutton;
    private TextInputLayout userId_layout;
    private TextInputLayout password_layout;
    private TextInputEditText userId;
    private TextInputEditText password;
    private static final String url = "https://fsb5uwp82c.execute-api.us-east-1.amazonaws.com";
    private static final boolean DEBUG = false;

    private void setErrorColor_userId(){
        userId_layout.setHelperText("required*");
        userId_layout.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, com.google.android.material.R.color.design_default_color_error)));

    }

    private void setErrorColor_password(){
        password_layout.setHelperText("required*");
        password_layout.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, com.google.android.material.R.color.design_default_color_error)));

    }

    private void reset_colors(){
        userId_layout.setHelperText("");
        userId_layout.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
        password_layout.setHelperText("");
        password_layout.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey)));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_view);

        userId = (TextInputEditText)findViewById(R.id.username_tiet);
        userId_layout = (TextInputLayout)findViewById((R.id.username_layout));
        password = (TextInputEditText)findViewById(R.id.password_tiet);
        password_layout = (TextInputLayout)findViewById(R.id.password_layout);

        loginbutton = findViewById(R.id.login);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DEBUG){
                    Intent intent = new Intent(Login.this, Catcher.class);
                    startActivity(intent);
                    return;
                }
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject();
                    jsonObj.put("userId", Objects.requireNonNull(userId.getText()).toString());
                    jsonObj.put("password", Objects.requireNonNull(password.getText()).toString());

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                LoginResponse response1 = LoginResponse.newInstance(LoginResponse.class,  response.toString());
                                if(response1.getError().equals("0")) {
                                    Intent intent = new Intent(Login.this, Catcher.class);
                                    intent.putExtra("userId", response1.getUserId());
                                    startActivity(intent);
                                }
                                else if (response1.getError().equals("1")){
                                    //username error
                                    Toast.makeText(getApplicationContext(), "Incorrect Username", Toast.LENGTH_LONG)
                                            .show();
                                    reset_colors();
                                    setErrorColor_userId();
                                }
                                else if(response1.getError().equals("2")){
                                    // password error
                                    Toast.makeText(getApplicationContext(), "Missing Password", Toast.LENGTH_LONG)
                                            .show();
                                    reset_colors();
                                    setErrorColor_password();
                                }
                                else if(response1.getError().equals("3")){
                                    // password error
                                    reset_colors();
                                    Toast.makeText(getApplicationContext(), "error code 500", Toast.LENGTH_LONG)
                                            .show();
                                }
                                else {
                                    //connection error?
                                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                    reset_colors();
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
        signupbutton = findViewById(R.id.signup);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

    }
}