package ru.xbitly.nolimy.db.entities.alien;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.xbitly.nolimy.db.entities.main.Card;

@Entity
public class AlienCard extends Card {
    public AlienCard(){}

    @NonNull
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getName() + "\n" + getDescription() + "\n\n");
        if(getContent() == null || getContent().entrySet().isEmpty()) return result.toString();
        for(int i = getContent().entrySet().size() - 1; i >= 0; i--) {
            List<Map.Entry<String, String>> entryList = new ArrayList<>(getContent().entrySet());
            Map.Entry<String, String> entry = entryList.get(i);
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return result.toString();
    }
}
