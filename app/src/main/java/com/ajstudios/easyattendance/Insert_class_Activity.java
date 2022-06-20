package com.ajstudios.easyattendance;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ajstudios.easyattendance.model.ResponseAttendance;
import com.ajstudios.easyattendance.model.ResponseMessge;
import com.ajstudios.easyattendance.model.User;
import com.ajstudios.easyattendance.retrofit.APIClient;
import com.ajstudios.easyattendance.retrofit.GetResult;
import com.ajstudios.easyattendance.utils.CustPrograssbar;
import com.ajstudios.easyattendance.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Objects;

import co.ceryle.radiorealbutton.library.RadioRealButton;
import co.ceryle.radiorealbutton.library.RadioRealButtonGroup;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class Insert_class_Activity extends AppCompatActivity implements GetResult.MyListener {

    Button create_button;
    EditText _subjectName;
    String[] departmentStr = {"CSE", "CIVIL", "IE", "ECE", "FET"};
    String[] semesterStr = {"1ST SEM", "2ND SEM", "3RD SEM", "4TH SEM", "5TH SEM", "6TH SEM", "7TH SEM", "8TH SEM"};

    Spinner spinnerDepartment ;
    Spinner spinnerSemester ;


    private  String position_bg = "0";
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    String semStr = "", deptStr = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_class_);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(Insert_class_Activity.this);
        user = sessionManager.getUserDetails("");

        Toolbar toolbar = findViewById(R.id.toolbar_insert_class);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        create_button = findViewById(R.id.button_createClass);
        _subjectName = findViewById(R.id.subjectName_createClass);
        spinnerDepartment = findViewById(R.id.spinnerDepartment);
        spinnerSemester = findViewById(R.id.spinnerSemester);

        final RadioRealButton button1 = (RadioRealButton) findViewById(R.id.button1);
        final RadioRealButton button2 = (RadioRealButton) findViewById(R.id.button2);
        final RadioRealButton button3 = (RadioRealButton) findViewById(R.id.button3);
        final RadioRealButton button4 = (RadioRealButton) findViewById(R.id.button4);
        final RadioRealButton button5 = (RadioRealButton) findViewById(R.id.button5);
        final RadioRealButton button6 = (RadioRealButton) findViewById(R.id.button6);

        RadioRealButtonGroup group = (RadioRealButtonGroup) findViewById(R.id.group);
        group.setOnClickedButtonPosition(new RadioRealButtonGroup.OnClickedButtonPosition() {
            @Override
            public void onClickedButtonPosition(int position) {
                position_bg = String.valueOf(position);
            }
        });

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid()) {

                    createClass();

                }else{
                    Toast.makeText(Insert_class_Activity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                }

                //-------

            }
        });



        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                deptStr = departmentStr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter deptAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, departmentStr);
        deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(deptAdapter);

        spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                semStr = semesterStr[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter semAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, semesterStr);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemester.setAdapter(semAdapter);

    }

    private void createClass() {

        custPrograssbar.prograssCreate(Insert_class_Activity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("subject_name", _subjectName.getText().toString().trim());
            jsonObject.put("subject_code", "123");
            jsonObject.put("semester", semStr);
            jsonObject.put("dept", deptStr);
            jsonObject.put("theme", position_bg);
            jsonObject.put("fk_teacher_id", user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().createClass(bodyRequest);
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
                ResponseMessge responseMessge = gson.fromJson(result.toString(), ResponseMessge.class);
                if (responseMessge.getResult().equals("true")) {

                    Toast.makeText(this, "" + responseMessge.getResponseMsg(), Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(this, "" + responseMessge.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("Errror", "==>" + e.toString());
        }


    }

    public boolean isValid(){

        return !_subjectName.getText().toString().isEmpty();
    }


}