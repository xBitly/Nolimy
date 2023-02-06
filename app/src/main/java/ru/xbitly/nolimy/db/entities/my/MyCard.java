package ru.xbitly.nolimy.db.entities.my;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @NonNull
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getName() + "\n" + getDescription() + "\n\n");
        if(getContent() == null || getContent().entrySet().isEmpty()) return result.toString();
        List<Map.Entry<String, String>> entryList = new ArrayList<>(getContent().entrySet());
        for(int i = getContent().entrySet().size() - 1; i >= 0; i--) {
            Map.Entry<String, String> entry = entryList.get(i);
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return result.toString();
    }
}
