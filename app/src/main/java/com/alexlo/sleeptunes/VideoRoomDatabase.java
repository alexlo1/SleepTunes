package com.alexlo.sleeptunes;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {VideoId.class}, version = 1)
public abstract class VideoRoomDatabase extends RoomDatabase {
    public abstract VideoIdDao videoIdDao();

    private static volatile VideoRoomDatabase INSTANCE;

    static VideoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VideoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            VideoRoomDatabase.class, "video_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
