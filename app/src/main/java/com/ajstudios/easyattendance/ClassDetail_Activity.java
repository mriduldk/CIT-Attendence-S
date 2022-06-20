package com.ajstudios.easyattendance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajstudios.easyattendance.Adapter.ClassListAdapter;
import com.ajstudios.easyattendance.Adapter.StudentsListAdapter;
import com.ajstudios.easyattendance.BottomSheet.MaxStudent;
import com.ajstudios.easyattendance.BottomSheet.Student_Edit_Sheet;
import com.ajstudios.easyattendance.model.LoginUser;
import com.ajstudios.easyattendance.model.ResponseClasses;
import com.ajstudios.easyattendance.model.ResponseMaxStudent;
import com.ajstudios.easyattendance.model.ResponseStudents;
import com.ajstudios.easyattendance.model.User;
import com.ajstudios.easyattendance.realm.Attendance_Reports;
import com.ajstudios.easyattendance.realm.Attendance_Students_List;
import com.ajstudios.easyattendance.realm.Students_List;
import com.ajstudios.easyattendance.retrofit.APIClient;
import com.ajstudios.easyattendance.retrofit.GetResult;
import com.ajstudios.easyattendance.utils.CustPrograssbar;
import com.ajstudios.easyattendance.utils.SessionManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ClassDetail_Activity extends AppCompatActivity implements GetResult.MyListener {

    private ImageView themeImage;
    private TextView className, total_students, place_holder, textViewMaxStudent;
    private CardView refreshAttendance, reports_open, cardMaxStudent;
    private Button submit_btn;
    private EditText student_name, reg_no, mobile_no;
    private LinearLayout layout_attendance_taken;
    private RecyclerView mRecyclerview;


    String subId, subject_Name, class_Name, studentCount, dept, maxValue = "";

    public static final String TAG = "ClassDetail_Activity";

    Realm realm;
    RealmAsyncTask transaction;
    RealmChangeListener realmChangeListener;

    private Handler handler = new Handler();
    StudentsListAdapter mAdapter;

    ProgressBar progressBar;
    Dialog lovelyCustomDialog;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail_);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(ClassDetail_Activity.this);
        user = sessionManager.getUserDetails("");

        getWindow().setExitTransition(null);
        Realm.init(this);

        final String theme = getIntent().getStringExtra("theme");
        class_Name = getIntent().getStringExtra("className");
        subject_Name = getIntent().getStringExtra("subjectName");
        subId = getIntent().getStringExtra("classroom_ID");
        studentCount = getIntent().getStringExtra("totalStudent");
        dept = getIntent().getStringExtra("dept");


        Toolbar toolbar = findViewById(R.id.toolbar_class_detail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_disease_detail);
        collapsingToolbarLayout.setTitle(subject_Name);

        themeImage = findViewById(R.id.image_disease_detail);
        className = findViewById(R.id.classname_detail);
        total_students = findViewById(R.id.total_students_detail);
        layout_attendance_taken = findViewById(R.id.attendance_taken_layout);
        layout_attendance_taken.setVisibility(View.GONE);
        refreshAttendance = findViewById(R.id.refreshAttendance);
        reports_open = findViewById(R.id.reports_open_btn);
        mRecyclerview = findViewById(R.id.recyclerView_detail);
        progressBar = findViewById(R.id.progressbar_detail);
        place_holder = findViewById(R.id.placeholder_detail);
        cardMaxStudent = findViewById(R.id.cardMaxStudent);
        textViewMaxStudent = findViewById(R.id.textViewMaxStudent);
        place_holder.setVisibility(View.GONE);
        submit_btn = findViewById(R.id.submit_attendance_btn);
        submit_btn.setVisibility(View.GONE);

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


        total_students.setText("Total Students : " + studentCount);
        className.setText(class_Name + " ( " + dept + " )");

        //---------------------------------

        Runnable r = new Runnable() {
            @Override
            public void run() {
                //RealmInit();
                //progressBar.setVisibility(View.GONE);
            }
        };
        handler.postDelayed(r, 500);

        //----------------------------------------

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*long count = realm.where(Students_List.class)
                        .equalTo("class_id", room_ID)
                        .count();
                final String size, size2;
                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ClassDetail_Activity.this);
                size = String.valueOf(preferences.getAll().size());
                size2 = String.valueOf(count);

                if (size.equals(size2)){
                    submitAttendance();
                }else {
                    Toast.makeText(ClassDetail_Activity.this, "Select all........", Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        reports_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassDetail_Activity.this, Reports_Activity.class);
                intent.putExtra("class_name", class_Name);
                intent.putExtra("subject_name", subject_Name);
                intent.putExtra("subId", subId);
                intent.putExtra("dept", dept);
                startActivity(intent);
            }
        });

        refreshAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadClasses();
                loadMaxStudent();

            }
        });


        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        cardMaxStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaxStudent maxStudent = new MaxStudent(subId, user.getId(), maxValue);
                maxStudent.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
                maxStudent.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(), "BottomSheet");

            }
        });


        loadClasses();
    }

    private void loadClasses() {

        custPrograssbar.prograssCreate(ClassDetail_Activity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dept", dept);
            jsonObject.put("sem", class_Name);
            jsonObject.put("subId", subId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getClassStudents(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    public void loadMaxStudent() {

        custPrograssbar.prograssCreate(ClassDetail_Activity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tid", user.getId());
            jsonObject.put("subId", subId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getMaxStudent(bodyRequest);
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
                ResponseStudents students = gson.fromJson(result.toString(), ResponseStudents.class);
                if (students.getResult().equals("true")) {

                    if (students.getStudentList() != null) {
                        mAdapter = new StudentsListAdapter(students.getStudentList() ,ClassDetail_Activity.this);
                        mRecyclerview.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(ClassDetail_Activity.this, "No Student found.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(ClassDetail_Activity.this, "" + students.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            } else if (callNo.equalsIgnoreCase("2")) {

                Gson gson = new Gson();
                ResponseMaxStudent students = gson.fromJson(result.toString(), ResponseMaxStudent.class);
                if (students.getResult().equals("true")) {

                    if (students.getMaxStudent() != null) {

                        textViewMaxStudent.setText(students.getMaxStudent().getValue() + " Student Allowed");

                        maxValue = students.getMaxStudent().getValue();

                    } else {
                        textViewMaxStudent.setText("Add Max Student");
                    }

                } else {
                    textViewMaxStudent.setText("Add Max Student");
                }
            }
        } catch (Exception e) {
            Log.e("Errror", "==>" + e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadMaxStudent();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public boolean isValid(){

        if (student_name.getText().toString().isEmpty() || reg_no.getText().toString().isEmpty() || mobile_no.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_class_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}