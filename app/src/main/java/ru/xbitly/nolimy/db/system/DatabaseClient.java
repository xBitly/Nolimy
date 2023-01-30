package ru.xbitly.nolimy.db.system;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseClient {

    private static DatabaseClient mInstance;
    private final AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "Records").build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
