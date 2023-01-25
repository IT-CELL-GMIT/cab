package com.codinglegend.legendsp.cabme.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codinglegend.legendsp.cabme.MainActivity;
import com.codinglegend.legendsp.cabme.R;
import com.codinglegend.legendsp.cabme.common;
import com.codinglegend.legendsp.cabme.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    Context context;
    String userName, password;
    String singStatus = "", logStatus = "";

    String loginApi = common.getBaseUrl() + "loginCabMe.php";
    String fetchUserDataApi = common.getBaseUrl() + "fetchUserData.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        context = LoginActivity.this;

        singStatus = common.getSharedPrf("SIGN_STATUS", context);
        logStatus = common.getSharedPrf("LOG_STATUS", context);

        if (singStatus != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SignUpActivity.class));
            }
        });

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = binding.userName.getText().toString().trim().toLowerCase();
                password = binding.password.getText().toString().trim();

                binding.userName.setText("");
                binding.password.setText("");

                common.showProgressDialog(context, "Checking Details...");

                checkLogin(userName, password);

            }
        });

    }

    private void checkLogin(String userName, String password) {

        StringRequest request = new StringRequest(Request.Method.POST, loginApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Loged in successfully") || response.contains("Loged in successfully")){
                            common.dismissProgresDialog();

                            common.showProgressDialog(context, "Getting Info...");

                            fetchUserData(userName, password);

                        }else {
                            common.showToast(context, "wrong login details");
                            common.dismissProgresDialog();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                common.showToast(context, "unable to login, try again later");
                common.dismissProgresDialog();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", userName);
                params.put("password", password);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    private void fetchUserData(String userName, String password) {

        StringRequest request = new StringRequest(Request.Method.POST, fetchUserDataApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (success.equals("1")){
                                String username, fullName, profilePic, phoneNumber, eMail, password;

                                JSONObject object = jsonArray.getJSONObject(0);

                                username = object.getString("username");
                                fullName = object.getString("full_name");
                                profilePic = object.getString("profile_pic");
                                phoneNumber = object.getString("mobile_no");
                                eMail = object.getString("e_mail");
                                password = object.getString("password");

                                if (userName.equals(username)){

                                    common.saveUsername(username, context);
                                    common.saveFullName(fullName, context);
                                    common.saveProfilePic(profilePic, context);
                                    common.saveMobileNo(phoneNumber, context);
                                    common.saveEMail(eMail, context);
                                    common.savePassword(password, context);


                                    common.setNameToSharedPref("SIGN_STATUS", "SIGNED", context);
                                    common.setNameToSharedPref("LOG_STATUS", "NONE", context);


                                    common.dismissProgresDialog();

                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();

                                }else {
                                    common.dismissProgresDialog();
                                    Toast.makeText(context, "Username of database is not matching try later", Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                common.dismissProgresDialog();
                                Toast.makeText(context, "not succeed try later", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            common.dismissProgresDialog();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "failed to retrive old data, try again later", Toast.LENGTH_SHORT).show();
                common.dismissProgresDialog();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("username", userName);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }



}