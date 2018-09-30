package com.alexlo.sleeptunes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.*;

/**
 * Main page fragment
 * Contains YouTube player option
 */
public class MainFragment extends Fragment
    implements com.google.android.youtube.player.YouTubePlayer.OnInitializedListener,
               com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener {

    private MainActivity activity;
    private Context context;
    private View rootView;

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    private Fragment youTubeFragment;

    private YouTubePlayer player;

    private TextView nowPlaying;

    /**
     * Fragment instantiates it UI view
     * @param inflater Inflates any views in the fragment
     * @param container If non-null, parent view to attach to
     * @param savedInstanceState If non-null, previous state to reconstruct
     * @return Fragment's UI view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        activity = (MainActivity) getActivity();
        context = getActivity().getApplicationContext();

        fragmentManager = getChildFragmentManager();

        initializeDisplay();
        initializeYoutube();

        return rootView;
    }

    /**
     * Initializes display elements
     */
    private void initializeDisplay() {
        nowPlaying = rootView.findViewById(R.id.nowPlaying);
        updateNowPlaying();
    }

    /**
     * Updates the now playing text every 0.1 seconds
     */
    private void updateNowPlaying() {
        final Handler nowPlayingHandler = new Handler();
        final Runnable nowPlayingRunnable = new Runnable() {
            public void run() {
                nowPlaying.setText(activity.mediaPlayer.getCurrentFileName());
                nowPlayingHandler.postDelayed(this, 100);
            }
        };
        nowPlayingHandler.postDelayed(nowPlayingRunnable, 100);
    }

    /**
     * Initializes the youtube player
     */
    private void initializeYoutube() {
        youTubeFragment = fragmentManager.findFragmentById(R.id.youtube_fragment);
        ((YouTubePlayerFragment) youTubeFragment).initialize(YouTubeMediaController.YOUTUBE_API_KEY, this);

        ft = fragmentManager.beginTransaction();
        if(!getSourcePreference().equals("youtube")) ft.hide(youTubeFragment);
        ft.commit();
    }

    /**
     * Show youtube player if youtube source is selected in settings
     * @param hidden True if the fragment is hidden
     */
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            ft = fragmentManager.beginTransaction();
            if (getSourcePreference().equals("youtube")) {
                ft.show(youTubeFragment);
            } else {
                if(!youTubeFragment.isHidden()) activity.setPlayButton(false);
                ft.hide(youTubeFragment);
            }
            ft.commit();
        }
    }

    /**
     * Gets the youtube player after it has been initialized
     * @return YouTube player
     */
    public YouTubePlayer getYouTubePlayer() {
        return player;
    }

    /**
     * Gets the media source specified in settings
     * @return String containing the chosen media source
     */
    private String getSourcePreference() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(getString(R.string.media_source_key), "ghibli");
    }


    /**
     * Called when initialization of the player succeeds
     * Required by OnInitializedListener interface
     * @param provider The provider initializing the player
     * @param player The player that controls video playback
     * @param wasRestored True if the user should expect to resume from a saved state
     */
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        this.player = player;
        activity.initializeYouTube(player);
        player.setPlaybackEventListener(this);
    }

    /**
     * Called when initialization of the player fails
     * Required by OnInitializedListener interface
     * @param provider The provider initializing the player
     * @param errorReason The reason for the failure, and with potential resolutions to this failure
     */
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(activity, 1).show();
        } else {
            Toast.makeText(activity, errorReason.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Required by PlaybackEventListener interface
     * @param isBuffering True if the player is buffering
     */
    public void onBuffering(boolean isBuffering) {}

    /**
     * Required by PlaybackEventListener interface
     */
    public void onPaused() {
        activity.setPlayButton(false);
    }

    /**
     * Required by PlaybackEventListener interface
     */
    public void onPlaying() {
        activity.setPlayButton(true);
    }

    /**
     * Required by PlaybackEventListener interface
     * @param newPositionMillis The time in milliseconds to which the player has seeked
     */
    public void onSeekTo(int newPositionMillis) {}

    /**
     * Required by PlaybackEventListener interface
     */
    public void onStopped() {}

}
