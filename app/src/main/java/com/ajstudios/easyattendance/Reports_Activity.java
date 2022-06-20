package com.ajstudios.easyattendance;

import static com.ajstudios.easyattendance.utils.SessionManager.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ajstudios.easyattendance.Adapter.ReportsAdapter;
import com.ajstudios.easyattendance.model.LoginUser;
import com.ajstudios.easyattendance.model.ResponseMaxStudent;
import com.ajstudios.easyattendance.model.User;
import com.ajstudios.easyattendance.realm.Attendance_Reports;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class Reports_Activity extends AppCompatActivity implements GetResult.MyListener {

    String subjectName, className, subId, dept;
    RecyclerView recyclerView;

    ReportsAdapter mAdapter;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(Reports_Activity.this);
        user = sessionManager.getUserDetails("");


        Realm.init(this);
        subjectName = getIntent().getStringExtra("subject_name");
        className = getIntent().getStringExtra("class_name");
        subId = getIntent().getStringExtra("subId");
        dept = getIntent().getStringExtra("dept");

        recyclerView = findViewById(R.id.recyclerView_reports);

        Toolbar toolbar = findViewById(R.id.toolbar_reports);
        setSupportActionBar(toolbar);
        toolbar.setTitle(subjectName);
        toolbar.setSubtitle(className + " ( " + dept + " )");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);

        recyclerView.setLayoutManager(gridLayoutManager);




        reportGet();
    }


    private void reportGet() {

        custPrograssbar.prograssCreate(Reports_Activity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tid", user.getId());
            jsonObject.put("subId", subId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getDailyReport(bodyRequest);
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
                ResponseMaxStudent dailyReport = gson.fromJson(result.toString(), ResponseMaxStudent.class);
                if (dailyReport.getResult().equals("true")) {

                    if (dailyReport.getMaxStudents() != null) {

                        mAdapter = new ReportsAdapter(dailyReport.getMaxStudents(),Reports_Activity.this, subjectName, className, dept);
                        recyclerView.setAdapter(mAdapter);

                    } else {
                        Toast.makeText(Reports_Activity.this, "" + dailyReport.getResponseMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(Reports_Activity.this, "" + dailyReport.getResponseMsg(), Toast.LENGTH_LONG).show();
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
        if(item.getItemId()==R.id.logout)
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            startActivity(new Intent(this, LoginActivity.class));
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}