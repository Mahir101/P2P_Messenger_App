package com.example.p2pmessagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread splashThread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);
                    Intent mainIntent = new Intent(SplashActivity.this,Main2Activity.class);
                    startActivity(mainIntent);
                    finish();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        splashThread.start();
    }
}
