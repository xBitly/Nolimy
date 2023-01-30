package ru.xbitly.nolimy.db.entities.alien;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AlienCardDao {

    @Query("SELECT * FROM AlienCard")
    List<AlienCard> getAll();

    @Insert
    void insert(AlienCard alienCard);

    @Delete
    void delete(AlienCard alienCard);

    @Update
    void update(AlienCard alienCard);

}
