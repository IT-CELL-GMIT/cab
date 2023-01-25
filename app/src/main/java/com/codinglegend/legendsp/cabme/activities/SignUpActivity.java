package com.codinglegend.legendsp.cabme.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.codinglegend.legendsp.cabme.databinding.ActivitySignUpBinding;
import com.codinglegend.legendsp.cabme.databinding.ActivitySignUpBindingImpl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    String fullName, userName, profileString = "null", phoneNumber, eMail, password;
    boolean profilePicSelect = false;
    Uri imagePath;
    String signStatus = "";

    Context context;

    String checkUsernameApi = common.getBaseUrl() + "userNameCheck.php";
    String userRegisterApi = common.getBaseUrl() + "userRegister.php";
    String fetchUseridApi = common.getBaseUrl() + "fetchUserId.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        context = SignUpActivity.this;

        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "please select your profile pic"), 1);

            }
        });

        binding.login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        binding.signup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.userName.getText().toString().trim().contains(" ")){
                    Toast.makeText(context, "spaces are not allowed in Username", Toast.LENGTH_SHORT).show();
                }
                else {

                    fullName = binding.fullName.getText().toString().trim();
                    userName = binding.userName.getText().toString().trim().toLowerCase();
                    phoneNumber = binding.phoneNumber.getText().toString().trim();
                    eMail = binding.eMail.getText().toString().trim();
                    password = binding.password.getText().toString().trim();

                    checkData();
                }

            }
        });

    }

    private void checkData() {

        if (fullName.length() < 5 || !fullName.contains(" ")){
            Toast.makeText(this, "Please enter FULLNAME for ex :\nSanjay Parmar", Toast.LENGTH_SHORT).show();
        }else if (userName.length() < 5){
            Toast.makeText(this, "please enter proper Username", Toast.LENGTH_SHORT).show();
        }else if (profileString.equalsIgnoreCase("null") || !profilePicSelect){
            Toast.makeText(this, "please select profile picture", Toast.LENGTH_SHORT).show();
        }else if (phoneNumber.length() < 10){
            Toast.makeText(this, "please enter proper phone number", Toast.LENGTH_SHORT).show();
        }else if (eMail.length() < 12 || !eMail.contains("@") || !eMail.contains(".com")){
            Toast.makeText(this, "please enter proper E-Mail for ex :\nhimanshu@gmail.com", Toast.LENGTH_SHORT).show();
        }else if (password.length() < 8){
            Toast.makeText(this, "please choose strong password of minimum 8 length", Toast.LENGTH_SHORT).show();
        }else {

            common.showProgressDialog(context, "checking data...");
            checkUserName();

        }

    }

    private void checkUserName() {

        StringRequest request = new StringRequest(Request.Method.POST, checkUsernameApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("user exist") || response.contains("user exist")){

                            Toast.makeText(context, "Select different Username", Toast.LENGTH_SHORT).show();
                            common.dismissProgresDialog();

                        }else {

                            uploadData();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "something went wrong, try again later", Toast.LENGTH_SHORT).show();
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

    private void uploadData() {

        StringRequest request = new StringRequest(Request.Method.POST, userRegisterApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equalsIgnoreCase("Data Inserted") || response.contains("Data Inserted")){

                            binding.fullName.setText("");
                            binding.userName.setText("");
                            binding.phoneNumber.setText("");
                            binding.eMail.setText("");
                            binding.password.setText("");

                            common.saveFullName(fullName, context);
                            common.saveUsername(userName, context);
                            common.saveMobileNo(phoneNumber, context);
                            common.saveEMail(eMail, context);
                            common.savePassword(password, context);
                            common.setNameToSharedPref("SIGN_STATUS", "SIGNED", context);



                            common.dismissProgresDialog();

                            fetchUserId();


                        }else {


                            common.showToast(context, "failed");
                            common.dismissProgresDialog();
                            finish();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, "something went wrong, try again later", Toast.LENGTH_SHORT).show();
                common.dismissProgresDialog();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("full_name", fullName);
                params.put("username", userName);
                params.put("e_mail", eMail);
                params.put("mobile_no", phoneNumber);
                params.put("password", password);
                params.put("profile_pic", profileString);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1
                && resultCode == RESULT_OK
                && data.getData() != null){

            imagePath = data.getData();
            binding.profileImageView.setImageURI(imagePath);

            profileString = common.ConvertToString(imagePath, context);
            Toast.makeText(context, profileString, Toast.LENGTH_SHORT).show();
            profilePicSelect = true;

        }else {
            Toast.makeText(this, "something went wrong try to select a picture try again", Toast.LENGTH_SHORT).show();
            imagePath = null;
        }

    }

    private void fetchUserId() {

        StringRequest request = new StringRequest(Request.Method.POST, fetchUseridApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success =jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if (success.equals("1")){

                                JSONObject object = jsonArray.getJSONObject(0);
                                String userId = object.getString("id");
                                String profilePic = object.getString("profilepic");

                                common.showToast(context, profilePic);

                                common.saveUserId(userId, context);
                                common.saveProfilePic(profilePic, context);
                                common.setNameToSharedPref("LOG_STATUS", "NONE", context);

                                common.dismissProgresDialog();

                                finish();

                            }

                        }catch (Exception e){
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            common.dismissProgresDialog();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
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