package com.alexlo.sleeptunes;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface VideoIdDao {

    @Insert
    void insert(VideoId videoId);

    @Delete
    void delete(VideoId videoId);

    @Query("DELETE from video_table")
    void deleteAll();

    @Query("SELECT * from video_table ORDER BY video_id ASC")
    LiveData<List<VideoId>> getAllVideos();
}
