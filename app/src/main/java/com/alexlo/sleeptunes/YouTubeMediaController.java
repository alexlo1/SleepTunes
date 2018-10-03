package com.alexlo.sleeptunes;

import com.google.android.youtube.player.YouTubePlayer;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaController extends MediaController {

    public static final String YOUTUBE_API_KEY = "AIzaSyBuzUdW70LFH_EbDhizBzzxZnRakcEwwaI";
    public static ArrayList<String> links;

    private YouTubePlayer player;
    private int currentVideo = 0;

    /**
     * Constructor for youtube media controller
     * @param player The player to control video playback
     */
    public YouTubeMediaController(YouTubePlayer player) {
        this.player = player;

        links = new ArrayList<>();
        links.add("7LEmer7wwHI");
        links.add("iBfk37Fa3H0");
        links.add("z3PpphdrEmU");
        links.add("9YNws6yE9Ns");

        initializeMediaPlayer();
    }

    /** Initialize the media player */
    public void initializeMediaPlayer() {
        player.cueVideo(YouTubeMediaController.links.get(0));
    }

    /**
     * Checks if media is playing
     * @return True if media is playing
     */
    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    /** Start/resume playing media */
    @Override
    public void play() {
        if(player != null && !player.isPlaying()) player.play();
    }

    /** Pause currently playing media */
    @Override
    public void pause() {
        if(player != null && player.isPlaying()) player.pause();
    }

    /** Stops and clears current media */
    @Override
    public void stop() {
        if(player != null) player.release();
    }

    /** Skip to next media file */
    public void next() {
        if(player != null) {
            currentVideo = (currentVideo + 1) % links.size();
            if(player.isPlaying()) {
                player.loadVideo(links.get(currentVideo));
            } else {
                player.cueVideo(links.get(currentVideo));
            }
        }
    }

    /** Rewind to beginning of media file or skip to previous media file*/
    public void previous() {
        if(player != null) {
            if (getTime() <= 3) {
                currentVideo = (currentVideo + links.size() - 1) % links.size();
                if(player.isPlaying()) {
                    player.loadVideo(links.get(currentVideo));
                } else {
                    player.cueVideo(links.get(currentVideo));
                }
            } else {
                seekTo(0);
            }
        }
    }

    /**
     * Jump to specified time in media file
     * @param secs Time to jump to in seconds
     */
    @Override
    public void seekTo(int secs) {
        if(player != null) player.seekToMillis(secs * 1000);
    }

    /**
     * Sets the volume of the media player
     * @param volume Volume scalar (0 to 1)
     */
    @Override
    public void setVolume(double volume) {

    }

    /**
     * Accessor for current media file time
     * @return Current media time in seconds
     */
    @Override
    public int getTime() {
        if(player != null) {
            return player.getCurrentTimeMillis() / 1000;
        } else {
            return -1;
        }
    }

    /**
     * Accessor for current media file duration
     * @return Current media duration in seconds
     */
    @Override
    public int getDuration() {
        if(player != null) {
            return player.getDurationMillis() / 1000;
        } else {
            return -1;
        }
    }

    /**
     * Accessor for current media file id
     * @return Current media id
     */
    public int getCurrentFile() {
        return currentVideo;
    }

    /**
     * Accessor for current media file name
     * @return Current media name
     */
    public String getCurrentFileName() {
        return "";
    }

    /**
     *
     * @param list
     */
    public void setLinks(List<VideoId> list) {
        links = new ArrayList<>();
        for(VideoId vid : list) {
            links.add(vid.getId());
        }
    }
}
