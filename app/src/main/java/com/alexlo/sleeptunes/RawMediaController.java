package com.alexlo.sleeptunes;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Media controller for raw media files (build into app)
 */
public class RawMediaController implements MediaController {

    private final Context context;
    private static MediaPlayer mp;
    private static int[] files = {R.raw.the_name_of_life, R.raw.promise_of_the_world, R.raw.path_of_the_wind, R.raw.a_town_with_an_ocean_view};
    private static String[] filenames = {"The Name of Life", "Promise of the World", "Path of the Wind", "A Town with an Ocean View"};
    private static int currentFile = 0;

    /**
     * Constructor
     * @param context Context of the activity
     * @param files List of file ids to play through
     */
    public RawMediaController(Context context, int[] files) {
        this.context = context.getApplicationContext();
        this.files = files;
        initializeMediaPlayer();
    }

    /** Initialize the media player */
    public void initializeMediaPlayer() {
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

    /** Start/resume playing media */
    public void play() {
        if (mp != null && !mp.isPlaying()) {
            mp.start();
        }
    }

    /** Pause currently playing media */
    public void pause() {
        if (mp != null && mp.isPlaying()) {
            mp.pause();
        }
    }

    /** Skip to next media file */
    public void next() {
        if (mp != null) {
            currentFile = (currentFile + 1) % files.length;
            stop();
            initializeMediaPlayer();
        }
    }

    /** Rewind to beginning of media file or skip to previous media file*/
    public void previous() {
        if (mp != null) {
            if (getTime() <= 3) {
                currentFile = (currentFile + files.length - 1) % files.length;
            }
            stop();
            initializeMediaPlayer();
        }
    }

    /** Stops and clears current media */
    public void stop() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    /**
     * Jump to specified time in media file
     * @param secs Time to jump to in seconds
     */
    public void seekTo(int secs) {
        mp.seekTo(secs * 1000);
    }

    /**
     * Accessor for current media file time
     * @return Current media time
     */
    public int getTime() {
        if(mp != null) {
            return mp.getCurrentPosition() / 1000;
        } else {
            return -1;
        }
    }

    /**
     * Accessor for current media file duration
     * @return Current media duration
     */
    public int getDuration() {
        if(mp != null) {
            return mp.getDuration() / 1000;
        } else {
            return -1;
        }
    }

    /**
     * Accessor for current media file id
     * @return Current media id
     */
    public int getCurrentFile() {
        return currentFile;
    }

    /**
     * Accessor for current media file name
     * @return Current media name
     */
    public String getCurrentFileName() {
        return filenames[currentFile];
    }
}
