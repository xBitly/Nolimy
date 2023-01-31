package ru.xbitly.nolimy.db.entities.alien;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Map;

import ru.xbitly.nolimy.db.entities.main.Card;

@Entity
public class AlienCard extends Card {
    public AlienCard(){}

    @NonNull
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getName() + "\n" + getDescription() + "\n\n");
        for(Map.Entry<String, String> entry: getContent().entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return result.toString();
    }
}
