package ru.xbitly.nolimy.db.entities.my;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import ru.xbitly.nolimy.db.entities.main.Card;

@Entity
public class MyCard extends Card {

    //TODO: converter for drawable
    @ColumnInfo(name = "background")
    private String background;

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
