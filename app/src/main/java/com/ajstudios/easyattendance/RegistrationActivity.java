package com.ajstudios.easyattendance;

import static com.ajstudios.easyattendance.utils.SessionManager.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, GetResult.MyListener {

    String[] userTypeArray = {"STUDENT", "TEACHER"};
    String[] departmentStr = {"CSE", "CIVIL", "IE", "ECE", "FET"};
    String[] semesterStr = {"1ST SEM", "2ND SEM", "3RD SEM", "4TH SEM", "5TH SEM", "6TH SEM", "7TH SEM", "8TH SEM"};
    Spinner spinnerUserType ;
    Spinner spinnerDepartment ;
    Spinner spinnerSemester ;
    EditText editTextMobile ;
    EditText editTextPass ;
    EditText editTextName;
    EditText editTextRollNo;
    TextView btnRegister ;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    TextView textViewRoll,textViewSem;

    String semStr = "", deptStr = "", usereType = "", fcm = "fcm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(RegistrationActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar_registration);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(this);

        spinnerUserType = findViewById(R.id.spinnerUserType);
        spinnerDepartment = findViewById(R.id.spinnerDepartment);
        spinnerSemester = findViewById(R.id.spinnerSemester);
        editTextName = findViewById(R.id.editTextName);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextRollNo = findViewById(R.id.editTextRollNo);
        editTextPass = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.textViewRegister);
        textViewRoll = findViewById(R.id.textViewRoll);
        textViewSem = findViewById(R.id.textViewSem);

        spinnerAdapters();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*OTP_Verify otp_verify = new OTP_Verify();
                otp_verify.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
                otp_verify.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(), "BottomSheet");*/

                registerUser();

            }
        });

    }

    private void spinnerAdapters() {

        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                usereType = userTypeArray[position];

                if (Objects.equals(usereType, "STUDENT")) {
                    editTextRollNo.setVisibility(View.VISIBLE);
                    spinnerSemester.setVisibility(View.VISIBLE);
                    textViewSem.setVisibility(View.VISIBLE);
                    textViewRoll.setVisibility(View.VISIBLE);
                } else {
                    editTextRollNo.setVisibility(View.GONE);
                    spinnerSemester.setVisibility(View.GONE);
                    textViewSem.setVisibility(View.GONE);
                    textViewRoll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter loginAsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypeArray);
        loginAsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(loginAsAdapter);

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

    private void registerUser() {

        custPrograssbar.prograssCreate(RegistrationActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", editTextName.getText().toString());
            jsonObject.put("phone", editTextMobile.getText().toString());
            jsonObject.put("password", editTextPass.getText().toString());
            jsonObject.put("rollno", editTextRollNo.getText().toString());
            jsonObject.put("semester", semStr);
            jsonObject.put("dept", deptStr);
            jsonObject.put("loginAs", usereType);
            jsonObject.put("fcm", fcm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().register(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @Override
    public void onClick(View view) {
        onBackPressed();
    }


    @Override
    public void callback(JsonObject result, String callNo) {

        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {

                Gson gson = new Gson();
                ResponseMessge response = gson.fromJson(result.toString(), ResponseMessge.class);
                if (response.getResult().equals("true")) {
                    //isvarification = 1;
                    User user = new User();
                    user.setName(editTextName.getText().toString());
                    user.setPhone_no(editTextMobile.getText().toString());
                    user.setId(response.getUserLogin());
                    user.setDepartment(deptStr);
                    user.setPassword(editTextPass.getText().toString());
                    user.setSemester(semStr);
                    user.setUserType(usereType);
                    sessionManager.setUserDetails("", user);
                    sessionManager.setBooleanData(login, true);

                    if (Objects.equals(usereType, "STUDENT")) {
                        Toast.makeText(RegistrationActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, StudentMainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }

                } else {
                    Toast.makeText(RegistrationActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("Errror", "==>" + e.toString());
        }
    }

}