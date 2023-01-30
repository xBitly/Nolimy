package ru.xbitly.nolimy.db.entities.alien;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import ru.xbitly.nolimy.db.system.DatabaseClient;

public class AlienCardEdit extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final AlienCard alienCard;

    public AlienCardEdit(Context context, AlienCard alienCard){
        this.context = context;
        this.alienCard = alienCard;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseClient.getInstance(context).getAppDatabase()
                .alienCardDao()
                .update(alienCard);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}