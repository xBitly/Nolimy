package ru.xbitly.nolimy.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import ru.xbitly.nolimy.db.entities.alien.AlienCard;
import ru.xbitly.nolimy.ui.fragments.ProfileFragment;
import ru.xbitly.nolimy.ui.fragments.UsersFragment;
import ru.xbitly.nolimy.R;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private boolean nfcIsSupported = true;

    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        ImageButton imageButtonRead = findViewById(R.id.button_read);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        ProfileFragment profileFragment = new ProfileFragment();
        UsersFragment usersFragment = new UsersFragment(this);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.users:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, usersFragment).commit();
                    return true;

                case R.id.profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, profileFragment).commit();
                    return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.users);

        if (nfcAdapter == null) {
            imageButtonRead.setImageDrawable(getDrawable(R.drawable.ic_qrcode_24));
            nfcIsSupported = false;
        }

        imageButtonRead.setOnClickListener(view -> {
            if (nfcAdapter != null) {
                Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                startActivity(intent);
                //TODO: anim
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }
}