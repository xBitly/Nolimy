package ru.xbitly.nolimy.db.system;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class Converter {
    @TypeConverter
    public static Map<String, String> fromString(String value) {
        Type listType = new TypeToken<Map<String, String>>(){}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(Map<String, String> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }
}
