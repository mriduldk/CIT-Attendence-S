package com.ajstudios.easyattendance;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

    }

}