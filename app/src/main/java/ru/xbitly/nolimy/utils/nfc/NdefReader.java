package ru.xbitly.nolimy.utils.nfc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.db.entities.alien.AlienCard;
import ru.xbitly.nolimy.db.entities.alien.AlienCardSave;
import ru.xbitly.nolimy.ui.elements.NolimySnackbar;
import ru.xbitly.nolimy.ui.recyclers.adapters.CardContentListAdapter;

public class NdefReader extends AsyncTask<Tag, Void, String> {
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
    private final TextView textViewReading;
    @SuppressLint("StaticFieldLeak")
    private final ImageView imageViewReading;

    public NdefReader(RelativeLayout relativeLayout, TextView textViewRead, Button buttonSave,
                      RecyclerView recyclerView, TextView textViewName, TextView textViewDescription,
                      Context context, TextView textViewReading, ImageView imageViewReading){
        this.relativeLayout = relativeLayout;
        this.textViewRead = textViewRead;
        this.buttonSave = buttonSave;
        this.recyclerView = recyclerView;
        this.textViewName = textViewName;
        this.textViewDescription = textViewDescription;
        this.context = context;
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