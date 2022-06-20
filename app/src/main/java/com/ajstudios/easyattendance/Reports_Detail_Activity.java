package com.ajstudios.easyattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ajstudios.easyattendance.Adapter.Reports_Detail_Adapter;
import com.ajstudios.easyattendance.Adapter.StudentsListAdapter;
import com.ajstudios.easyattendance.model.ResponseMaxStudent;
import com.ajstudios.easyattendance.model.ResponseStudents;
import com.ajstudios.easyattendance.model.User;
import com.ajstudios.easyattendance.realm.Attendance_Students_List;
import com.ajstudios.easyattendance.retrofit.APIClient;
import com.ajstudios.easyattendance.retrofit.GetResult;
import com.ajstudios.easyattendance.utils.CustPrograssbar;
import com.ajstudios.easyattendance.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class Reports_Detail_Activity extends AppCompatActivity implements GetResult.MyListener {

    RecyclerView recyclerView;
    Reports_Detail_Adapter mAdapter;

    TextView subj, className, toolbar_title;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;

    String subId;
    String classname;
    String subjName;
    String date;
    String dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports__detail);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(Reports_Detail_Activity.this);
        user = sessionManager.getUserDetails("");

        subId = getIntent().getStringExtra("ID");
        classname = getIntent().getStringExtra("class");
        subjName = getIntent().getStringExtra("subject");
        date = getIntent().getStringExtra("date");
        dept = getIntent().getStringExtra("dept");

        Toolbar toolbar = findViewById(R.id.toolbar_reports_detail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView_reports_detail);
        subj = findViewById(R.id.subjName_report_detail);
        className = findViewById(R.id.classname_report_detail);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(date);
        subj.setText(subjName);
        className.setText(classname + " ( " + dept + " )");


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadReport();
    }

    private void loadReport() {

        custPrograssbar.prograssCreate(Reports_Detail_Activity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dept", dept);
            jsonObject.put("sem", classname);
            jsonObject.put("subId", subId);
            jsonObject.put("date", date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().dailySubReport(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

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
                        mAdapter = new Reports_Detail_Adapter( students.getStudentList() ,Reports_Detail_Activity.this);
                        recyclerView.setAdapter(mAdapter);

                    } else {
                        Toast.makeText(Reports_Detail_Activity.this, "No Student found.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(Reports_Detail_Activity.this, "" + students.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("Errror", "==>" + e.toString());
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.only_dot, menu);
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