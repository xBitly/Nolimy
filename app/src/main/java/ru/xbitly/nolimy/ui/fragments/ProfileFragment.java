package ru.xbitly.nolimy.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ru.xbitly.nolimy.R;
import ru.xbitly.nolimy.db.entities.my.MyCard;
import ru.xbitly.nolimy.db.entities.my.MyCardGet;
import ru.xbitly.nolimy.db.entities.my.MyCardSave;
import ru.xbitly.nolimy.ui.activities.CreateActivity;
import ru.xbitly.nolimy.ui.activities.WriteActivity;
import ru.xbitly.nolimy.ui.elements.NolimySnackbar;
import ru.xbitly.nolimy.ui.pagers.adapters.CardsPagerAdapter;

public class ProfileFragment extends Fragment {

    private final Context context;
    private MyCardGet myCardGet;

    private NfcAdapter nfcAdapter;
    private boolean nfcIsSupported = true;
    private ViewPager pager;
    private RelativeLayout relativeLayoutNoCards;
    private ImageButton buttonCreate;
    private ImageButton buttonEdit;
            NestedScrollView relativeLayout;
    private TextView textViewName;
    private TextView textViewDescription;
    private RecyclerView recyclerView;
    private Button buttonWrite;

    public ProfileFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pager = view.findViewById(R.id.pager);
        relativeLayoutNoCards = view.findViewById(R.id.relative_no_cards);
        buttonCreate = view.findViewById(R.id.button_create);
        buttonEdit = view.findViewById(R.id.button_edit);
        relativeLayout = view.findViewById(R.id.scroll_view);
        textViewName = view.findViewById(R.id.text_name);
        textViewDescription = view.findViewById(R.id.text_description);
        recyclerView = view.findViewById(R.id.recycler);
        buttonWrite = view.findViewById(R.id.button_write);

        nfcAdapter = NfcAdapter.getDefaultAdapter(context);

        if (nfcAdapter == null) {
            buttonWrite.setVisibility(View.GONE);
            nfcIsSupported = false;
        }

        buttonEdit.setOnClickListener(view1 -> {
            MyCard myCard = myCardGet.getMyCard();
            Intent intent = new Intent(getActivity(), CreateActivity.class);
            intent.putExtra("card", myCard);
            startActivity(intent);
        });

        buttonCreate.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), CreateActivity.class);
            startActivity(intent);
        });

        buttonWrite.setOnClickListener(view1 -> {
            if (!nfcIsSupported) return;
            if (!nfcAdapter.isEnabled()){
                NolimySnackbar snackbar = new NolimySnackbar();
                snackbar.createInfoSnackbar(context, view);
                snackbar.getTextInfo().setText(getText(R.string.nfc_disabled));
                snackbar.show();
                startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                return;
            }

            MyCard myCard = myCardGet.getMyCard();
            myCard.setId(0);

            Gson gson = new Gson();

            Intent intent = new Intent(getActivity(), WriteActivity.class);
            intent.putExtra("json", gson.toJson(myCard));
            startActivity(intent);
            //TODO: anim

        });

    }

    @Override
    public void onResume(){
        super.onResume();
        pager.setAdapter(new CardsPagerAdapter(null, context));

        myCardGet = new MyCardGet(context, relativeLayoutNoCards, textViewName, textViewDescription,
                recyclerView, relativeLayout, buttonWrite);
        myCardGet.setViewPager(pager);
        myCardGet.execute();
    }
}