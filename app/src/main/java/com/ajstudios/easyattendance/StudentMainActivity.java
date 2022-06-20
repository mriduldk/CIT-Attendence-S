package com.ajstudios.easyattendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajstudios.easyattendance.Adapter.ClassListAdapterStudent;
import com.ajstudios.easyattendance.model.ResponseClasses;
import com.ajstudios.easyattendance.model.User;
import com.ajstudios.easyattendance.retrofit.APIClient;
import com.ajstudios.easyattendance.retrofit.GetResult;
import com.ajstudios.easyattendance.utils.CustPrograssbar;
import com.ajstudios.easyattendance.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class StudentMainActivity extends AppCompatActivity implements GetResult.MyListener {

    RecyclerView recyclerView;
    TextView sample, textViewUserName;

    ClassListAdapterStudent mAdapter;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_activity_main);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(StudentMainActivity.this);
        user = sessionManager.getUserDetails("");

        Toolbar toolbar = findViewById(R.id.toolbar_beginner);
        setSupportActionBar(toolbar);

        getWindow().setEnterTransition(null);

        sample = findViewById(R.id.classes_sample);
        textViewUserName = findViewById(R.id.textViewUserName);
        recyclerView = findViewById(R.id.recyclerView_main);

        recyclerView.setHasFixedSize(true);
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);


        textViewUserName.setText("Welcome Back, " + user.getName());

        loadClasses();
    }

    private void loadClasses() {

        custPrograssbar.prograssCreate(StudentMainActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dept", user.getDepartment());
            jsonObject.put("sem", user.getSemester());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().studentIndividualClass(bodyRequest);
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
                ResponseClasses classes = gson.fromJson(result.toString(), ResponseClasses.class);
                if (classes.getResult().equals("true")) {

                    if (classes.getClassesList() != null) {
                        mAdapter = new ClassListAdapterStudent(classes.getClassesList() , StudentMainActivity.this);
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(StudentMainActivity.this, "No classes found. Please add one.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(StudentMainActivity.this, "" + classes.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("Errror", "==>" + e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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