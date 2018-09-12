package com.alexlo.sleeptunes;

import com.google.android.youtube.player.YouTubePlayer;

public class YouTubeMediaController {

    public static final String YOUTUBE_API_KEY = "AIzaSyBuzUdW70LFH_EbDhizBzzxZnRakcEwwaI";
    private YouTubePlayer player;

    /**
     * Constructor for youtube media controller
     * @param player The player to control video playback
     */
    public YouTubeMediaController(YouTubePlayer player) {
        this.player = player;
    }

}
