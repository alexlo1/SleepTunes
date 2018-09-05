package com.alexlo.sleeptunes;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Main page fragment
 * Doesn't do much
 */
public class MainFragment extends Fragment {

    private MainActivity activity;
    private Context context;
    private View rootView;
    private boolean active = false;

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
        active = true;

        initializeDisplay();

        return rootView;
    }

    /**
     * Initializes display elements
     */
    private void initializeDisplay() {
        nowPlaying = activity.findViewById(R.id.nowPlaying);
        updateNowPlaying();
    }

    /**
     * Updates the now playing text
     */
    private void updateNowPlaying() {
        nowPlaying.setText(getString(R.string.now_playing_text, activity.mediaPlayer.getCurrentFileName()));
    }
}
