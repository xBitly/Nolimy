package ru.xbitly.nolimy.db.entities.alien;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.xbitly.nolimy.db.system.DatabaseClient;
import ru.xbitly.nolimy.ui.recycler.AlienCardsListAdapter;

public class AlienCardGet extends AsyncTask<Void, Void, List<AlienCard>> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private RecyclerView recyclerView;

    public AlienCardGet(Context context){
        this.context = context;
    }

    @Override
    protected List<AlienCard> doInBackground(Void... voids) {
        return DatabaseClient.getInstance(context).getAppDatabase()
                .alienCardDao()
                .getAll();
    }

    @Override
    protected void onPostExecute(List<AlienCard> alienCardList) {
        super.onPostExecute(alienCardList);
        AlienCardsListAdapter adapter = new AlienCardsListAdapter(alienCardList, context);
        if (recyclerView != null) recyclerView.setAdapter(adapter);
    }

    public void setRecyclerView(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }
}