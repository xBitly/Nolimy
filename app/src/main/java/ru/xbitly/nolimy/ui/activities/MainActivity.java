package ru.xbitly.nolimy.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import ru.xbitly.nolimy.db.entities.alien.AlienCard;
import ru.xbitly.nolimy.db.entities.alien.AlienCardSave;
import ru.xbitly.nolimy.ui.fragments.ProfileFragment;
import ru.xbitly.nolimy.ui.fragments.UsersFragment;
import ru.xbitly.nolimy.R;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        ImageButton imageButtonRead = findViewById(R.id.read);

        ProfileFragment profileFragment = new ProfileFragment();
        UsersFragment usersFragment = new UsersFragment(this);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
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

        imageButtonRead.setOnClickListener(view -> {

            AlienCard alienCard = new AlienCard();
            alienCard.setName("Andrey Tester");
            alienCard.setDescription("Tester");
            Map<String, String> map = new HashMap<>();
            map.put("Number", "+79163918498");
            map.put("Telegram", "avandarte.t.me");
            alienCard.setContent(map);
            AlienCardSave alienCardSave = new AlienCardSave(this, alienCard);
            alienCardSave.execute();

        });
    }
}