package ru.xbitly.nolimy.db.entities.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

import ru.xbitly.nolimy.db.system.DatabaseClient;
import ru.xbitly.nolimy.ui.pagers.adapters.CardsPagerAdapter;
import ru.xbitly.nolimy.ui.recyclers.adapters.AlienCardsListAdapter;
import ru.xbitly.nolimy.ui.recyclers.adapters.CardContentListAdapter;

public class MyCardGet extends AsyncTask<Void, Void, List<MyCard>> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private final RelativeLayout relativeLayoutNoCards;
    @SuppressLint("StaticFieldLeak")
    private ViewPager viewPager;
    @SuppressLint("StaticFieldLeak")
    private final TextView textViewName;
    @SuppressLint("StaticFieldLeak")
    private final TextView textViewDescription;
    @SuppressLint("StaticFieldLeak")
    private final RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    private final RelativeLayout relativeLayout;
    @SuppressLint("StaticFieldLeak")
    private final Button buttonWrite;
    private CardsPagerAdapter cardsPagerAdapter;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    private List<MyCard> myCards;

    public MyCardGet(Context context, RelativeLayout relativeLayoutNoCards, TextView textViewName,
                     TextView textViewDescription, RecyclerView recyclerView, RelativeLayout relativeLayout,
                     Button buttonWrite){
        this.context = context;
        this.relativeLayoutNoCards = relativeLayoutNoCards;
        this.textViewName = textViewName;
        this.textViewDescription = textViewDescription;
        this.recyclerView = recyclerView;
        this.preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        this.editor = preferences.edit();
        this.relativeLayout = relativeLayout;
        this.buttonWrite = buttonWrite;
    }

    @Override
    protected List<MyCard> doInBackground(Void... voids) {
        return DatabaseClient.getInstance(context).getAppDatabase()
                .myCardDao()
                .getAll();
    }

    @Override
    protected void onPostExecute(List<MyCard> myCardList) {
        super.onPostExecute(myCardList);

        this.myCards = myCardList;

        int[] resIds = new int[myCardList != null ? myCardList.size() : 0];
        for (int i = 0; i < resIds.length; i++) {
            MyCard myCard = myCardList.get(i);
            resIds[i] = myCard.getBackground();
        }
        cardsPagerAdapter = new CardsPagerAdapter(resIds, context);
        if (viewPager != null && resIds.length != 0){
            relativeLayout.setVisibility(View.VISIBLE);
            buttonWrite.setVisibility(View.VISIBLE);
            relativeLayoutNoCards.setVisibility(View.GONE);
            viewPager.setAdapter(cardsPagerAdapter);
            viewPager.addOnPageChangeListener(pageChangeListener);
            viewPager.setPageTransformer(false, (view, positionOffset) -> view.setAlpha(1 - positionOffset));
            int position = preferences.getInt("position_my_card", 0);
            viewPager.setCurrentItem(position);
            textViewName.setText(myCards.get(position).getName());
            textViewDescription.setText(myCards.get(position).getDescription());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new CardContentListAdapter(myCards.get(position).getContent(), context));
        }

        buttonWrite.setOnClickListener(view -> {

        });
    }

    public void setViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
    }

    public PagerAdapter getAdapter() {
        return cardsPagerAdapter;
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (myCards == null) return;
            textViewName.setText(myCards.get(position).getName());
            textViewDescription.setText(myCards.get(position).getDescription());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new CardContentListAdapter(myCards.get(position).getContent(), context));
            editor.putInt("position_my_card", position);
            editor.apply();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
