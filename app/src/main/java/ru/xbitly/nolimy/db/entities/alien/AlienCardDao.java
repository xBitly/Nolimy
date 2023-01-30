package ru.xbitly.nolimy.db.entities.alien;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
