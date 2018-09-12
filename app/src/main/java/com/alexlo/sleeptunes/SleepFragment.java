package com.alexlo.sleeptunes;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Sleep page fragment
 * Contains sleep timer display
 */
public class SleepFragment extends Fragment {

    private MainActivity activity;
    private Context context;
    private View rootView;

    private Button sleepTimer;
    private Button resetTimer;
    private int previousSetting;
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
        rootView = inflater.inflate(R.layout.fragment_sleep, container, false);
        activity = (MainActivity) getActivity();
        context = getActivity().getApplicationContext();

        initializeSleepTimer();
        initializeResetButton();
        initializeNowPlaying();

        return rootView;
    }

    /**
     * Updates the sleep timer display when the fragment is shown
     * @param hidden True if the fragment is hidden
     */
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) updateSleepTimer();
    }

    /**
     * Initializes the sleep timer display
     */
    private void initializeSleepTimer() {
        sleepTimer = rootView.findViewById(R.id.sleepTimer);
        sleepTimer.setText(convertTime(getDurationPreference()));
        previousSetting = getDurationPreference();

        sleepTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.flipCountDown();
            }
        });

        // update sleep timer display every 0.1 seconds
        final Handler sleepTimerHandler = new Handler();
        final Runnable seekTimerRunnable = new Runnable() {
            public void run() {
                if(activity.getCountDown()) {
                    sleepTimer.setText(convertTime(activity.getSleepTimer()));
                }
                sleepTimerHandler.postDelayed(this, 100);
            }
        };
        sleepTimerHandler.postDelayed(seekTimerRunnable, 100);
    }

    /**
     * Updates the sleep timer display
     */
    private void updateSleepTimer() {
        if(previousSetting != getDurationPreference() && !activity.getCountDown()) {
            activity.setSleepTimer(getDurationPreference());
            sleepTimer.setText(convertTime(activity.getSleepTimer()));
            previousSetting = getDurationPreference();
        }
    }

    /**
     * Initializes the sleep timer reset button
     */
    private void initializeResetButton() {
        resetTimer = rootView.findViewById(R.id.resetSleepTimer);
        resetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.setSleepTimer(getDurationPreference());
                activity.setCountDown(false);
                sleepTimer.setText(convertTime(getDurationPreference()));
            }
        });
    }

    /**
     * Updates the now playing text every 0.1 seconds
     */
    private void initializeNowPlaying() {
        nowPlaying = rootView.findViewById(R.id.nowPlaying);
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
     * Gets the sleep timer duration specified in settings
     * @return Integer containing the current sleep timer duration setting in seconds
     */
    private int getDurationPreference() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(sharedPref.getString(getString(R.string.sleep_timer_time_key), "1200")) * 60;
    }

    /**
     * Converts an integer number of seconds into a time string
     * @param t Time to convert
     * @return String in the form {min}:{sec} (xx:xx)
     */
    private String convertTime(int t) {
        String sec = String.valueOf(t % 60);
        String min = String.valueOf(t / 60);
        if (sec.length() == 1) sec = '0' + sec;
        if (min.length() == 1) min = '0' + min;
        return getString(R.string.time_string, min, sec);
    }
}
