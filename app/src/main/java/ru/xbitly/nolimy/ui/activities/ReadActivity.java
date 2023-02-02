package ru.xbitly.nolimy.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.db.entities.alien.AlienCard;
import ru.xbitly.nolimy.db.entities.alien.AlienCardSave;
import ru.xbitly.nolimy.ui.elements.NolimySnackbar;
import ru.xbitly.nolimy.ui.recyclers.adapters.CardContentListAdapter;

public class ReadActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private RelativeLayout relativeLayout;
    private RelativeLayout relative;
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

        relative = findViewById(R.id.relative);
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

    private static class NdefReader extends AsyncTask<Tag, Void, String> {
        @SuppressLint("StaticFieldLeak")
        private final RelativeLayout relativeLayout;
        @SuppressLint("StaticFieldLeak")
        private final TextView textViewRead;
        @SuppressLint("StaticFieldLeak")
        private final Button buttonSave;
        @SuppressLint("StaticFieldLeak")
        private final RecyclerView recyclerView;
        @SuppressLint("StaticFieldLeak")
        private final TextView textViewName;
        @SuppressLint("StaticFieldLeak")
        private final TextView textViewDescription;
        @SuppressLint("StaticFieldLeak")
        private final Context context;
        @SuppressLint("StaticFieldLeak")
        private final View view;
        @SuppressLint("StaticFieldLeak")
        private final TextView textViewReading;
        @SuppressLint("StaticFieldLeak")
        private final ImageView imageViewReading;

        public NdefReader(RelativeLayout relativeLayout, TextView textViewRead, Button buttonSave,
                          RecyclerView recyclerView, TextView textViewName, TextView textViewDescription,
                          Context context, View view, TextView textViewReading, ImageView imageViewReading){
            this.relativeLayout = relativeLayout;
            this.textViewRead = textViewRead;
            this.buttonSave = buttonSave;
            this.recyclerView = recyclerView;
            this.textViewName = textViewName;
            this.textViewDescription = textViewDescription;
            this.context = context;
            this.view = view;
            this.imageViewReading = imageViewReading;
            this.textViewReading = textViewReading;
        }

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                return null;
            }
            NdefMessage ndefMessage = ndef.getCachedNdefMessage();
            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException ignored) {}
                }
            }
            return null;
        }
        private String readText(NdefRecord record) throws UnsupportedEncodingException {
            byte[] payload = record.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageCodeLength = payload[0] & 51;
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                setCardVisible();
                Gson gson = new Gson();
                AlienCard alienCard = gson.fromJson(result, AlienCard.class);
                setCardFields(alienCard);

                buttonSave.setOnClickListener(view -> {
                    AlienCardSave alienCardSave = new AlienCardSave(context, alienCard);
                    alienCardSave.execute();

                    NolimySnackbar nolimySnackbar = new NolimySnackbar();
                    nolimySnackbar.createSuccessSnackbar(context, view);
                    nolimySnackbar.getTextSuccess().setText(context.getText(R.string.saved));
                    nolimySnackbar.show();

                    setCardInvisible();
                });
            }
        }

        public void setCardVisible(){
            relativeLayout.setVisibility(View.VISIBLE);
            textViewRead.setVisibility(View.VISIBLE);
            buttonSave.setVisibility(View.VISIBLE);
            textViewReading.setVisibility(View.GONE);
            imageViewReading.setVisibility(View.GONE);
        }

        public void setCardInvisible(){
            relativeLayout.setVisibility(View.GONE);
            textViewRead.setVisibility(View.GONE);
            buttonSave.setVisibility(View.GONE);
            textViewReading.setVisibility(View.VISIBLE);
            imageViewReading.setVisibility(View.VISIBLE);
        }

        public void setCardFields(AlienCard alienCard){
            textViewName.setText(alienCard.getName());
            textViewDescription.setText(alienCard.getDescription());
            recyclerView.setAdapter(new CardContentListAdapter(alienCard.getContent(), context));
        }
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
                new NdefReader(relativeLayout, textViewRead, buttonSave, recyclerView, textViewName, textViewDescription, this, relative, textViewReading, imageViewReading).execute(tag);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReader(relativeLayout, textViewRead, buttonSave, recyclerView, textViewName, textViewDescription, this, relative, textViewReading, imageViewReading).execute(tag);
                    break;
                }
            }
        }
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        @SuppressLint("UnspecifiedImmutableFlag")
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
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