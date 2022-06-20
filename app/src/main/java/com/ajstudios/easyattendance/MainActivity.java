package com.ajstudios.easyattendance;

import static com.ajstudios.easyattendance.utils.SessionManager.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ajstudios.easyattendance.Adapter.ClassListAdapter;
import com.ajstudios.easyattendance.model.LoginUser;
import com.ajstudios.easyattendance.model.ResponseClasses;
import com.ajstudios.easyattendance.model.User;
import com.ajstudios.easyattendance.realm.Class_Names;
import com.ajstudios.easyattendance.retrofit.APIClient;
import com.ajstudios.easyattendance.retrofit.GetResult;
import com.ajstudios.easyattendance.utils.CustPrograssbar;
import com.ajstudios.easyattendance.utils.SessionManager;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements GetResult.MyListener {

    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    RecyclerView recyclerView;
    TextView sample;

    ClassListAdapter mAdapter;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(MainActivity.this);
        user = sessionManager.getUserDetails("");

        Toolbar toolbar = findViewById(R.id.toolbar_beginner);
        setSupportActionBar(toolbar);

        Realm.init(this);
        getWindow().setEnterTransition(null);

        bottomAppBar = findViewById(R.id.bottomAppBar);
        fab_main = findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(MainActivity.this, Insert_class_Activity.class));
            }
        });

        realm = Realm.getDefaultInstance();

        RealmResults<Class_Names> results;

        results = realm.where(Class_Names.class)
                .findAll();


        sample = findViewById(R.id.classes_sample);
        recyclerView = findViewById(R.id.recyclerView_main);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

    }

    private void loadClasses() {

        custPrograssbar.prograssCreate(MainActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("teacher_id", user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getClasses(bodyRequest);
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
                        mAdapter = new ClassListAdapter(classes.getClassesList() ,MainActivity.this);
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(MainActivity.this, "No classes found. Please add one.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "" + classes.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("Errror", "==>" + e.toString());
        }
    }

    @Override
    protected void onResume() {
        loadClasses();
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