package com.willpasco.clickies.dao;

import android.content.Context;

import com.willpasco.clickies.model.Movie;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1)
public abstract class ClickiesRoomDatabase extends RoomDatabase {

    private static volatile ClickiesRoomDatabase INSTACE;

    public static ClickiesRoomDatabase getDatabase(final Context context) {
        if (INSTACE == null) {
            synchronized (ClickiesRoomDatabase.class) {
                if (INSTACE == null) {
                    INSTACE = Room.databaseBuilder(context.getApplicationContext(),
                            ClickiesRoomDatabase.class,
                            "clickies_database")
                            .build();
                }
            }
        }
        return INSTACE;
    }

    public abstract MovieDao movieDao();
}
