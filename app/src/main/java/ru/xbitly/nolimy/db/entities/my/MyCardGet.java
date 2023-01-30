package ru.xbitly.nolimy.db.entities.my;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ru.xbitly.nolimy.db.system.DatabaseClient;

public class MyCardGet extends AsyncTask<Void, Void, List<MyCard>> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;

    public MyCardGet(Context context){
        this.context = context;
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
    }
}
