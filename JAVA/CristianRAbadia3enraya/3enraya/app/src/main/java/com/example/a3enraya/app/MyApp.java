package com.example.a3enraya.app;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class MyApp extends Application {
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
