package com.ajstudios.easyattendance;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ajstudios.easyattendance.Adapter.StudentsListAdapter;
import com.ajstudios.easyattendance.model.ResponseAttendance;
import com.ajstudios.easyattendance.model.ResponseMessge;
import com.ajstudios.easyattendance.model.ResponseStudents;
import com.ajstudios.easyattendance.model.User;
import com.ajstudios.easyattendance.retrofit.APIClient;
import com.ajstudios.easyattendance.retrofit.GetResult;
import com.ajstudios.easyattendance.utils.CustPrograssbar;
import com.ajstudios.easyattendance.utils.SessionManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AttendanceActivity extends AppCompatActivity implements GetResult.MyListener {

    private ImageView themeImage;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    private TextView className, total_students, textViewMaxStudent;
    private LinearLayout attendance_taken_layout, btnMarkPresent;
    String subId, subject_Name, class_Name, studentCount, dept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);


        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(AttendanceActivity.this);
        user = sessionManager.getUserDetails("");


        final String theme = getIntent().getStringExtra("theme");
        class_Name = getIntent().getStringExtra("className");
        subject_Name = getIntent().getStringExtra("subjectName");
        subId = getIntent().getStringExtra("classroom_ID");
        studentCount = getIntent().getStringExtra("totalStudent");
        dept = getIntent().getStringExtra("dept");

        Toolbar toolbar = findViewById(R.id.toolbar_attendance);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_disease_detail);
        collapsingToolbarLayout.setTitle(subject_Name);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        themeImage = findViewById(R.id.image_disease_detail);
        className = findViewById(R.id.classname_detail);
        total_students = findViewById(R.id.total_students_detail);
        attendance_taken_layout = findViewById(R.id.attendance_taken_layout);
        btnMarkPresent = findViewById(R.id.btnMarkPresent);



        attendance_taken_layout.setVisibility(View.GONE);

        total_students.setText("Total Students : " + studentCount);
        className.setText(class_Name + " ( " + dept + " )");

        switch (theme) {
            case "0":
                themeImage.setImageResource(R.drawable.asset_bg_paleblue);
                break;
            case "1":
                themeImage.setImageResource(R.drawable.asset_bg_green);

                break;
            case "2":
                themeImage.setImageResource(R.drawable.asset_bg_yellow);

                break;
            case "3":
                themeImage.setImageResource(R.drawable.asset_bg_palegreen);

                break;
            case "4":
                themeImage.setImageResource(R.drawable.asset_bg_paleorange);

                break;
            case "5":
                themeImage.setImageResource(R.drawable.asset_bg_white);
                break;

        }


        btnMarkPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addAttendances();
            }
        });


        loadAttendances();

    }

    private void loadAttendances() {

        custPrograssbar.prograssCreate(AttendanceActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", user.getId());
            jsonObject.put("subId", subId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getStudentAttendance(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void addAttendances() {

        custPrograssbar.prograssCreate(AttendanceActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sid", user.getId());
            jsonObject.put("subId", subId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().addAttendance(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }


    @Override
    public void callback(JsonObject result, String callNo) {

        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {

                Gson gson = new Gson();
                ResponseAttendance attendance = gson.fromJson(result.toString(), ResponseAttendance.class);
                if (attendance.getResult().equals("true")) {

                    if (attendance.getAttendance() != null) {

                        attendance_taken_layout.setVisibility(View.VISIBLE);
                        btnMarkPresent.setVisibility(View.GONE);

                    } else {

                        attendance_taken_layout.setVisibility(View.GONE);
                        btnMarkPresent.setVisibility(View.VISIBLE);
                    }

                } else {
                    attendance_taken_layout.setVisibility(View.GONE);
                    btnMarkPresent.setVisibility(View.VISIBLE);
                }
            } else if (callNo.equalsIgnoreCase("2")) {

                Gson gson = new Gson();
                ResponseMessge attendance = gson.fromJson(result.toString(), ResponseMessge.class);
                if (attendance.getResult().equals("true")) {

                    attendance_taken_layout.setVisibility(View.VISIBLE);
                    btnMarkPresent.setVisibility(View.GONE);

                    Toast.makeText(this, "" + attendance.getResponseMsg(), Toast.LENGTH_LONG).show();

                } else {
                    attendance_taken_layout.setVisibility(View.GONE);
                    btnMarkPresent.setVisibility(View.VISIBLE);

                    Toast.makeText(this, "" + attendance.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("Errror", "==>" + e.toString());
        }


    }
}