package ru.xbitly.nolimy.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import ru.xbitly.nolimy.R;

public class HelloActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final SharedPreferences settings = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor settingsEditor = settings.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        Button buttonSkip = findViewById(R.id.button_skip);

        buttonSkip.setOnClickListener(view -> {
            settingsEditor.putBoolean("first_start", false);
            settingsEditor.apply();
            Intent intent = new Intent(HelloActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        });
    }
}