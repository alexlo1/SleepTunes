package com.alexlo.sleeptunes;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {
    private VideoRepository repository;

    private LiveData<List<VideoId>> allVideos;

    public VideoViewModel (Application application) {
        super(application);
        repository = new VideoRepository(application);
        allVideos = repository.getAllWords();
    }

    LiveData<List<VideoId>> getAllWords() { return allVideos; }

    public void insert(VideoId id) { repository.insert(id); }
}
