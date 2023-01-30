package ru.xbitly.nolimy.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        boolean firstStart = settings.getBoolean("first_start", true);

        super.onCreate(savedInstanceState);

        Intent intent = firstStart ?
                new Intent(SplashActivity.this, HelloActivity.class) :
                new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}