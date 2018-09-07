package com.alexlo.sleeptunes;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Media controller for raw media files (build into app)
 */
public class RawMediaController extends MediaController {

    private final Context context;
    private int[] files;
    private String[] filenames;
    private int currentFile = 0;

    /**
     * Constructor
     * @param context Context of the activity
     * @param files List of file ids to play through
     * @param filenames List of file names
     */
    public RawMediaController(Context context, int[] files, String[] filenames) {
        this.context = context.getApplicationContext();
        this.files = files;
        this.filenames = filenames;
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
