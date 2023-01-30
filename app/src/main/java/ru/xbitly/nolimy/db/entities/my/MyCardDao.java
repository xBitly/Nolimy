package ru.xbitly.nolimy.db.entities.my;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

public interface MyCardDao {

    @Query("SELECT * FROM MyCard")
    List<MyCard> getAll();

    @Insert
    void insert(MyCard myCard);

    @Delete
    void delete(MyCard myCard);

    @Update
    void update(MyCard myCard);

}