package ru.xbitly.nolimy.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.utils.nfc.NdefReader;

public class ReadActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private RelativeLayout relativeLayout;
    private TextView textViewName;
    private TextView textViewDescription;
    private TextView textViewRead;
    private TextView textViewReading;
    private ImageView imageViewReading;
    private RecyclerView recyclerView;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        ImageButton buttonBack = findViewById(R.id.button_back);

        relativeLayout = findViewById(R.id.relative_center);
        textViewRead = findViewById(R.id.text_read);
        textViewReading = findViewById(R.id.text_reading);
        imageViewReading = findViewById(R.id.image_reading);
        textViewName = findViewById(R.id.text_name);
        buttonSave = findViewById(R.id.button_save);
        textViewDescription = findViewById(R.id.text_description);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonBack.setOnClickListener(view -> {
            Intent intent = new Intent(ReadActivity.this, MainActivity.class);
            startActivity(intent);
            //TODO: anim
            overridePendingTransition(0, 0);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) setupForegroundDispatch(this, nfcAdapter);
    }

    @Override
    protected void onPause() {
        if (nfcAdapter != null) stopForegroundDispatch(this, nfcAdapter);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReader(relativeLayout, textViewRead, buttonSave, recyclerView, textViewName, textViewDescription, this, textViewReading, imageViewReading).execute(tag);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReader(relativeLayout, textViewRead, buttonSave, recyclerView, textViewName, textViewDescription, this, textViewReading, imageViewReading).execute(tag);
                    break;
                }
            }
        }
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException ignored) {}
        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
}