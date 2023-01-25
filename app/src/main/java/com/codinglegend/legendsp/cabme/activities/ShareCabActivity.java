package com.codinglegend.legendsp.cabme.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codinglegend.legendsp.cabme.R;
import com.codinglegend.legendsp.cabme.common;
import com.codinglegend.legendsp.cabme.databinding.ActivityShareCabBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ShareCabActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener{

    private ActivityShareCabBinding binding;
    private Context context;

    RadioGroup availableSpaceRG, typeRG, acNoneAcRG;
    RadioButton radioButton1, radioButton2, radioButton3;

    String startingAddress = "", endingAddress = "", startingTime = "", endingTime = "";
    String cabName = "", cabModel = "", cabDriverMo = "", cabVehicalNum = "";
    String availableSpace = "", cabType = "", cabAcNoneAc = "";
    String uniqueCabId;

    Boolean stTimeBoll = false, endTimeBool = false;

    private DatePickerDialog dpd;
    private TimePickerDialog tpd;

    String dateTime;

    String addCabApi = common.getBaseUrl() + "addCabToShare.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_cab);
        context = ShareCabActivity.this;

        availableSpaceRG = findViewById(R.id.availableSpaceRG);
        typeRG = findViewById(R.id.typeRG);
        acNoneAcRG = findViewById(R.id.acNoneAcRG);

        binding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        binding.selectStartingTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stTimeBoll = true;
                endTimeBool = false;

                selectStartintTime();
            }
        });

        binding.selectEndingTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stTimeBoll = false;
                endTimeBool = true;

                selectStartintTime();
            }
        });

    }

    private void selectStartintTime() {

        Calendar now = Calendar.getInstance();

        if (dpd == null) {
            dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                    (DatePickerDialog.OnDateSetListener) context,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        } else {
            dpd.initialize(
                    (DatePickerDialog.OnDateSetListener) context,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        }
        dpd.setThemeDark(true);
        dpd.setCancelable(false);

        dpd.setOnCancelListener(dialog -> {
            Log.d("DatePickerDialog", "Dialog was cancelled");
            dpd = null;
        });
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");

    }

    private void getData() {


        startingAddress = binding.edStartingAddress.getText().toString().trim();
        endingAddress = binding.endingAddress.getText().toString().trim();
        cabName = binding.edCarName.getText().toString().trim();
        cabModel = binding.edCarModel.getText().toString().trim();
        cabDriverMo = binding.edDriverNo.getText().toString().trim();
        cabVehicalNum = binding.edVehicalNo.getText().toString().trim();


        int ID1 = availableSpaceRG.getCheckedRadioButtonId();
        int ID2 = typeRG.getCheckedRadioButtonId();
        int ID3 = acNoneAcRG.getCheckedRadioButtonId();

        radioButton1 = findViewById(ID1);
        radioButton2 = findViewById(ID2);
        radioButton3 = findViewById(ID3);

        if (startingAddress.length() < 3){
            common.showToast(context, "fill starting address");
        }else if (endingAddress.length() < 3){
            common.showToast(context, "fill ending address");
        }else if (startingTime.length() < 3){
            common.showToast(context, "select starting time");
        }else if (endingTime.length() < 3){
            common.showToast(context, "select ending time");
        }else if (cabName.length() < 3){
            common.showToast(context, "fill car name");
        }else if (cabModel.length() < 3){
            common.showToast(context, "fill cab model");
        }else if (cabDriverMo.length() < 3){
            common.showToast(context, "fill driver contact");
        }else if (cabVehicalNum.length() < 3){
            common.showToast(context, "fill vehical number");
        }else if (findViewById(ID1) == null){
            common.showToast(context, "select available space");
        }else if (findViewById(ID2) == null){
            common.showToast(context, "select car type");
        }else if (findViewById(ID3) == null){
            common.showToast(context, "select AC/noneAC");
        }else {

            common.showProgressDialog(context, "Please wait...");

            availableSpace = radioButton1.getText().toString();
            cabType = radioButton2.getText().toString();
            cabAcNoneAc = radioButton3.getText().toString();

            uniqueCabId = common.getUserName(context) + "_" + cabName.replace(" ", "") + "_" + String.valueOf(System.currentTimeMillis());

            shareCab();

        }

//        if (findViewById(ID) == null){
//            common.showToast(context, "select every radio group");
//        }else {
//
//            radioButton = findViewById(ID);
//            common.showToast(context, radioButton.getText().toString());
//        }

    }

    private void shareCab() {

        StringRequest request = new StringRequest(Request.Method.POST, addCabApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        common.dismissProgresDialog();
                        if (response.equalsIgnoreCase("failed") || response.contains("failed")){
                            common.showToast(context, response);
                        }
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                common.dismissProgresDialog();
                common.showToast(context, "connection error");
                finish();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("cab_id", uniqueCabId);
                params.put("from_address", startingAddress);
                params.put("to_address", endingAddress);
                params.put("cab_owner", common.getUserName(context));
                params.put("status", "ACTIVE");
                params.put("starting_time", startingTime);
                params.put("ending_time", endingTime);
                params.put("cab_name", cabName);
                params.put("cab_model", cabModel);
                params.put("cab_driver_mo", cabDriverMo);
                params.put("cab_vehical_no", cabVehicalNum);
                params.put("available_space", availableSpace);
                params.put("cab_type", cabType);
                params.put("ac_noneac", cabAcNoneAc);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String y = String.valueOf(year);
        String month = String.valueOf(monthOfYear + 1);
        String day = String.valueOf(dayOfMonth);

        dateTime = day + "-" + month + "_" + y;

        Calendar now = Calendar.getInstance();

        if (tpd == null) {
            tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                    (TimePickerDialog.OnTimeSetListener) context,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true
            );
        } else {
            tpd.initialize(
                    (TimePickerDialog.OnTimeSetListener) context,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    now.get(Calendar.SECOND),
                    true
            );
        }
        tpd.setThemeDark(true);
        tpd.vibrate(true);
        tpd.setCancelable(false);
        tpd.setOnCancelListener(dialogInterface -> {
            Log.d("TimePicker", "Dialog was cancelled");
            tpd = null;
        });
        tpd.show(getSupportFragmentManager(), "Timepickerdialog");

    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {

        String hour = String.valueOf(hourOfDay);
        String min = String.valueOf(minute);
        String sec = String.valueOf(second);

        dateTime = dateTime + " , " +hour + ":" + min;

        if (stTimeBoll){
            binding.selectStartingTimeBtn.setText(dateTime);
            startingTime = dateTime;
        }else {
            binding.selectEndingTimeBtn.setText(dateTime);
            endingTime = dateTime;
        }

    }
}