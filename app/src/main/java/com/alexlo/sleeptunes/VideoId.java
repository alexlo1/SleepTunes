package com.alexlo.sleeptunes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "video_table")
public class VideoId {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "video_id")
    private String id;

    public VideoId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}

