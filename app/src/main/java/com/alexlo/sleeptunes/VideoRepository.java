package com.alexlo.sleeptunes;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class VideoRepository {
    private VideoIdDao videoIdDao;
    private LiveData<List<VideoId>> mAllWords;

    VideoRepository(Application application) {
        VideoRoomDatabase db = VideoRoomDatabase.getDatabase(application);
        videoIdDao = db.videoIdDao();
        mAllWords = videoIdDao.getAllVideos();
    }

    LiveData<List<VideoId>> getAllWords() {
        return mAllWords;
    }


    public void insert (VideoId id) {
        new insertAsyncTask(videoIdDao).execute(id);
    }

    private static class insertAsyncTask extends AsyncTask<VideoId, Void, Void> {

        private VideoIdDao mAsyncTaskDao;

        insertAsyncTask(VideoIdDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final VideoId... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
