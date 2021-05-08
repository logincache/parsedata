package com.example.parsedata;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class LoadScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedinstanceStatetate) {
        super.onCreate(savedinstanceStatetate);
        setContentView(R.layout.load_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                 startActivity(new Intent(LoadScreen.this,MainActivity.class));
                 finish();
            }
        }, 4000);
    }

}
