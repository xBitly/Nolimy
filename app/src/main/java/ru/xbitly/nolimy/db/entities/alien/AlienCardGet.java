package ru.xbitly.nolimy.db.entities.alien;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import ru.xbitly.nolimy.db.system.DatabaseClient;
import ru.xbitly.nolimy.ui.recyclers.adapters.AlienCardsListAdapter;

public class AlienCardGet extends AsyncTask<Void, Void, List<AlienCard>> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    private TextView textView;
    private AlienCardsListAdapter adapter;

    public AlienCardGet(Context context, TextView textView){
        this.context = context;
        this.textView = textView;
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
        adapter = new AlienCardsListAdapter(alienCardList, context);
        if (recyclerView != null) recyclerView.setAdapter(adapter);
        if (adapter.getItemCount() == 0) textView.setVisibility(View.VISIBLE);
        else textView.setVisibility(View.GONE);
    }

    public void setRecyclerView(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }

    public AlienCardsListAdapter getAdapter() {
        return adapter;
    }
}