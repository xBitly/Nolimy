package ru.xbitly.nolimy.db.entities.alien;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import ru.xbitly.nolimy.db.system.DatabaseClient;

public class AlienCardGet extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final AlienCard alienCard;

    public AlienCardGet(Context context, AlienCard alienCard){
        this.context = context;
        this.alienCard = alienCard;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseClient.getInstance(context).getAppDatabase()
                .alienCardDao()
                .getAll();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}