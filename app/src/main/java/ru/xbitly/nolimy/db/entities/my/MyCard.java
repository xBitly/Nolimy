package ru.xbitly.nolimy.db.entities.my;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.graphics.drawable.Drawable;

import ru.xbitly.nolimy.db.entities.main.Card;

@Entity
public class MyCard extends Card {

    @ColumnInfo(name = "background")
    private Drawable background;

    public Drawable getBackground() {
        return background;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }
}
