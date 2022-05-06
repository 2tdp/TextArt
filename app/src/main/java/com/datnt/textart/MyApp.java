package com.datnt.textart;

import android.app.Application;

import com.datnt.textart.sharepref.DataLocalManager;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        DataLocalManager.init(getApplicationContext());

        super.onCreate();
    }
}
