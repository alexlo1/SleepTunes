package com.alexlo.sleeptunes;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {VideoId.class}, version = 1)
public abstract class VideoRoomDatabase extends RoomDatabase {
    public abstract VideoIdDao videoIdDao();

    private static volatile VideoRoomDatabase INSTANCE;

    private static RoomDatabase.Callback sRoomDatabaseCallback =
        new RoomDatabase.Callback(){

            @Override
            public void onOpen (@NonNull SupportSQLiteDatabase db){
                super.onOpen(db);
                new PopulateDbAsync(INSTANCE).execute();
            }
        };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final VideoIdDao dao;

        PopulateDbAsync(VideoRoomDatabase db) {
            dao = db.videoIdDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            dao.deleteAll();
            VideoId id = new VideoId("Hello");
            dao.insert(id);
            id = new VideoId("World");
            dao.insert(id);
            return null;
        }
    }

    static VideoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VideoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            VideoRoomDatabase.class, "video_database")
                                .addCallback(sRoomDatabaseCallback)
                                .build();
                }
            }
        }
        return INSTANCE;
    }
}
