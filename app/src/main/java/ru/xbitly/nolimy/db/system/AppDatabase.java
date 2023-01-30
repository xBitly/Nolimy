package ru.xbitly.nolimy.db.system;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.xbitly.nolimy.db.entities.alien.AlienCard;
import ru.xbitly.nolimy.db.entities.alien.AlienCardDao;
import ru.xbitly.nolimy.db.entities.my.MyCard;
import ru.xbitly.nolimy.db.entities.my.MyCardDao;

@Database(entities = {MyCard.class, AlienCard.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AlienCardDao alienCardDao();
    public abstract MyCardDao myCardDao();
}
