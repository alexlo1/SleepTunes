package com.alexlo.sleeptunes;

public interface MediaController {
    void initializeMediaPlayer();
    void play();
    void pause();
    void next();
    void previous();
    void seekTo(int secs);
    int getTime();
    int getDuration();
    int getCurrentFile();
}
