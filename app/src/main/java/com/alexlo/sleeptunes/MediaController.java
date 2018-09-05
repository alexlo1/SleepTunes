package com.alexlo.sleeptunes;

/** Media controller interface */
public interface MediaController {

    /** Initialize the media player */
    void initializeMediaPlayer();

    /** Start/resume playing media */
    void play();

    /** Pause currently playing media */
    void pause();

    /** Skip to next media file */
    void next();

    /** Rewind to beginning of media file or skip to previous media file*/
    void previous();

    /** Stops and clears current media */
    void stop();

    /**
     * Jump to specified time in media file
     * @param secs Time to jump to in seconds
     */
    void seekTo(int secs);

    /**
     * Accessor for current media file time
     * @return Current media time
     */
    int getTime();

    /**
     * Accessor for current media file duration
     * @return Current media duration
     */
    int getDuration();

    /**
     * Accessor for current media file id
     * @return Current media id
     */
    int getCurrentFile();

    /**
     * Accessor for current media file name
     * @return Current media name
     */
    String getCurrentFileName();
}
