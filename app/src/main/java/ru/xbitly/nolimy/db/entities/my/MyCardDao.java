package ru.xbitly.nolimy.db.entities.my;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
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