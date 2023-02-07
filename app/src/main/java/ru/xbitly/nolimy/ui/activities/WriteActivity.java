package ru.xbitly.nolimy.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.ui.elements.NolimySnackbar;

public class WriteActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private String json;
    private RelativeLayout relativeLayout;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        ImageButton buttonBack = findViewById(R.id.button_back);
        Button buttonSupport = findViewById(R.id.button_support);

        json = getIntent().getStringExtra("json");

        relativeLayout = findViewById(R.id.relative);

        buttonBack.setOnClickListener(view -> onBackPressed());

        buttonSupport.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, "nolimy.dev@gmail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Nolimy Appeal");
            startActivity(intent);
        });
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NolimySnackbar nolimySnackbar = new NolimySnackbar();
            if(writeTag(tag, createTextMessage(json))){
                nolimySnackbar.createSuccessSnackbar(this, relativeLayout);
                nolimySnackbar.getTextSuccess().setText(getText(R.string.success));
                nolimySnackbar.show();
            } else {
                nolimySnackbar.createErrorSnackbar(this, relativeLayout);
                nolimySnackbar.getTextError().setText(getText(R.string.error));
                nolimySnackbar.show();
            }
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(WriteActivity.this, MainActivity.class);
        editor.putInt("fragment", R.id.profile);
        editor.apply();
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
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

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        @SuppressLint("UnspecifiedImmutableFlag")
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    public NdefMessage createTextMessage(String content) {
        try {
            byte[] lang = Locale.getDefault().getLanguage().getBytes(StandardCharsets.UTF_8);
            byte[] text = content.getBytes(StandardCharsets.UTF_8);

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                    NdefRecord.RTD_TEXT, new byte[0],
                    payload.toByteArray());
            return new NdefMessage(new NdefRecord[]{record});
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean writeTag(Tag tag, NdefMessage message)  {
        if (tag != null) {
            try {
                Ndef ndefTag = Ndef.get(tag);
                if (ndefTag == null) {
                    NdefFormatable nForm = NdefFormatable.get(tag);
                    if (nForm != null) {
                        nForm.connect();
                        nForm.format(message);
                        nForm.close();
                        return true;
                    }
                }
                else {
                    ndefTag.connect();
                    ndefTag.writeNdefMessage(message);
                    ndefTag.close();
                    return true;
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}