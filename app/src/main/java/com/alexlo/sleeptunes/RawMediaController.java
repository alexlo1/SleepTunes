package com.alexlo.sleeptunes;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class RawMediaController implements MediaController {

    private static final String TAG = "RawMediaController";

    private final Context context;
    private static MediaPlayer mp;
    private static int[] files = {R.raw.test1, R.raw.test2, R.raw.test3};
    private static int currentFile = 0;

    public RawMediaController(Context context, int[] files) {
        this.context = context.getApplicationContext();
        this.files = files;
        initializeMediaPlayer();
    }

    public void initializeMediaPlayer() {
        Log.d(TAG, "Initializing media player");
        mp = MediaPlayer.create(context, files[currentFile]);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                currentFile = (currentFile + 1) % files.length;
                initializeMediaPlayer();
                play();
            }
        });
    }

    public void play() {
        if (mp != null && !mp.isPlaying()) {
            Log.d(TAG, "Start/Resume playing media");
            mp.start();
        }
    }

    public void pause() {
        if (mp != null && mp.isPlaying()) {
            Log.d(TAG, "Pausing media");
            mp.pause();
        }
    }

    public void next() {
        if (mp != null) {
            currentFile = (currentFile + 1) % files.length;
            stop();
            initializeMediaPlayer();
        }
    }

    public void previous() {
        if (mp != null) {
            Log.d(TAG, String.valueOf(getTime()));
            if (getTime() <= 3) {
                currentFile = (currentFile + files.length - 1) % files.length;
            }
            stop();
            initializeMediaPlayer();
        }
    }

    public void stop() {
        if (mp != null) {
            Log.d(TAG, "Stopping media");
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    public void seekTo(int secs) {
        mp.seekTo(secs * 1000);
    }

    public int getTime() {
        if(mp != null) {
            return mp.getCurrentPosition() / 1000;
        } else {
            return -1;
        }
    }

    public int getDuration() {
        if(mp != null) {
            return mp.getDuration() / 1000;
        } else {
            return -1;
        }
    }

    public int getCurrentFile() {
        return currentFile;
    }

    public String getCurrentFileName() {
        return "test"+(currentFile+1);
    }
}
