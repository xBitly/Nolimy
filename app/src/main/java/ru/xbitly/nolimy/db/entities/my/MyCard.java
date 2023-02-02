package ru.xbitly.nolimy.db.entities.my;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import ru.xbitly.nolimy.db.entities.main.Card;

@Entity
public class MyCard extends Card {

    @ColumnInfo(name = "background")
    private int background;

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
