package com.alexlo.sleeptunes;

import android.media.MediaPlayer;

/** Media controller interface */
public abstract class MediaController {

    protected MediaPlayer mp;

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
     * @return Current media time in seconds
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
     * @return Current media duration in seconds
     */
    public int getDuration() {
        if(mp != null) {
            return mp.getDuration() / 1000;
        } else {
            return -1;
        }
    }

    /** Initialize the media player */
    abstract void initializeMediaPlayer();

    /** Skip to next media file */
    abstract void next();

    /** Rewind to beginning of media file or skip to previous media file*/
    abstract void previous();

    /**
     * Accessor for current media file id
     * @return Current media id
     */
    abstract int getCurrentFile();

    /**
     * Accessor for current media file name
     * @return Current media name
     */
    abstract String getCurrentFileName();
}
