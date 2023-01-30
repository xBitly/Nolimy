package ru.xbitly.nolimy.db.entities.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import ru.xbitly.nolimy.db.system.DatabaseClient;

public class MyCardGet extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private final MyCard myCard;

    public MyCardGet(Context context, MyCard myCard){
        this.context = context;
        this.myCard = myCard;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseClient.getInstance(context).getAppDatabase()
                .myCardDao()
                .getAll();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
