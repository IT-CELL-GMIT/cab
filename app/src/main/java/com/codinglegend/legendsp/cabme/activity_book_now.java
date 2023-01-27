package com.codinglegend.legendsp.cabme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codinglegend.legendsp.cabme.databinding.ActivityBookNowBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class activity_book_now extends AppCompatActivity {

    private ActivityBookNowBinding binding;
    private Context context;

    String cabId;

    String fetchCabDetailsApi = common.getBaseUrl() + "FetchCabDetails.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_now);
        context = activity_book_now.this;

        Intent intent = getIntent();
        cabId = intent.getStringExtra("cabId");

        getCabDetails();

    }

    private void getCabDetails() {

        common.showProgressDialog(context, "Please wait...");

        StringRequest request = new StringRequest(Request.Method.POST, fetchCabDetailsApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        common.dismissProgresDialog();

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            String success = jsonObject.getString("success");

                            if (success.equalsIgnoreCase("1")){

                                for (int i=0; i<jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    binding.startingAddress.setText(object.getString("from_address"));
                                    binding.endingAddress.setText(object.getString("to_address"));
                                    binding.selectStartingTimeBtn.setText(object.getString("starting_time"));
                                    binding.selectEndingTimeBtn.setText(object.getString("ending_time"));
                                    binding.tvCarName.setText(object.getString("cab_name"));
                                    binding.tvCarModel.setText(object.getString("cab_model"));
                                    binding.tvDriverContact.setText(object.getString("cab_driver_mo"));
                                    binding.tvVehicalNum.setText(object.getString("cab_vehical_no"));
                                    binding.tvNoOfSeats.setText(object.getString("available_space"));
                                    binding.tvType.setText(object.getString("cab_type"));
                                    binding.tvAcNoneAc.setText(object.getString("ac_noneac"));

                                }

                            }

                        } catch (JSONException e) {
                            Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("cab_id", cabId);

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }
}