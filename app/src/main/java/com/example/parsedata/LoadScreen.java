package com.example.parsedata;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class LoadScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedinstanceStatetate) {
        super.onCreate(savedinstanceStatetate);
        setContentView(R.layout.load_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();//Full Screen Activity in Android

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                 startActivity(new Intent(LoadScreen.this,MainActivity.class));
                 finish();
            }
        }, 2000);
    }

}
