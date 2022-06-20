package com.ajstudios.easyattendance;

import static com.ajstudios.easyattendance.utils.SessionManager.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.ajstudios.easyattendance.model.User;
import com.ajstudios.easyattendance.utils.SessionManager;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {


    SessionManager sessionManager;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        sessionManager = new SessionManager(SplashScreenActivity.this);
        user = sessionManager.getUserDetails("");

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (sessionManager.getBooleanData(login)) {

                    if (Objects.equals(user.getUserType(), "STUDENT")) {
                        startActivity(new Intent(SplashScreenActivity.this, StudentMainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        finish();
                    }

                } else {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 1500);

    }
}