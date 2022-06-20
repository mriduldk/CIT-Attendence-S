package com.ajstudios.easyattendance;

import static com.ajstudios.easyattendance.utils.SessionManager.login;

import androidx.appcompat.app.AppCompatActivity;

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

import com.ajstudios.easyattendance.model.LoginUser;
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

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GetResult.MyListener {

    String[] loginAs = {"STUDENT", "TEACHER"};
    Spinner spinnerLoginAs ;
    EditText editTextMobile ;
    EditText editTextPass ;
    TextView btnLogin ;
    TextView btnRegister ;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    String userType = "", fcm = "fcm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(LoginActivity.this);

        spinnerLoginAs = findViewById(R.id.spinnerLoginAs);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextPass = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.textViewBtnLogin);
        btnRegister = findViewById(R.id.textViewRegister);

        spinnerLoginAs.setOnItemSelectedListener(this);
        ArrayAdapter loginAsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, loginAs);
        loginAsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoginAs.setAdapter(loginAsAdapter);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUser();

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });

    }


    private void loginUser() {

        custPrograssbar.prograssCreate(LoginActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", editTextMobile.getText().toString());
            jsonObject.put("password", editTextPass.getText().toString());
            jsonObject.put("loginAs", userType);
            jsonObject.put("fcm", fcm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().loginUser(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        //Toast.makeText(getApplicationContext(), loginAs[position], Toast.LENGTH_LONG).show();
        userType = loginAs[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void callback(JsonObject result, String callNo) {

        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {

                Gson gson = new Gson();
                LoginUser loginUser = gson.fromJson(result.toString(), LoginUser.class);
                if (loginUser.getResult().equals("true")) {
                    sessionManager.setUserDetails("", loginUser.getUser());
                    sessionManager.setBooleanData(login, true);

                    if (Objects.equals(loginUser.getUser().getUserType(), "STUDENT")) {
                        Toast.makeText(LoginActivity.this, "" + loginUser.getResponseMsg(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, StudentMainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "" + loginUser.getResponseMsg(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "" + loginUser.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("Errror", "==>" + e.toString());
        }
    }




}