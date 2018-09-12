package com.alexlo.sleeptunes;

import com.google.android.youtube.player.YouTubePlayer;

public class YouTubeMediaController extends MediaController {

    public static final String YOUTUBE_API_KEY = "AIzaSyBuzUdW70LFH_EbDhizBzzxZnRakcEwwaI";
    private YouTubePlayer player;

    public YouTubeMediaController(YouTubePlayer player) {
        this.player = player;
    }

    public void initializeMediaPlayer() {

    }

    @Override
    public void play() {
        if(player != null) player.play();
    }

    @Override
    public void pause() {
        if(player != null) player.pause();
    }

    @Override
    public void stop() {
        if(player != null) player.release();
    }

    public void next() {
        if(player != null) player.next();
    }

    public void previous() {
        if(player != null) player.previous();
    }

    @Override
    public void seekTo(int secs) {

    }

    @Override
    public void setVolume(double volume) {

    }

    @Override
    public int getTime() {
        return 0;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    public int getCurrentFile() {
        return 0;
    }

    public String getCurrentFileName() {
        return "";
    }
}
