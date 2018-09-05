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

/**
 * Sleep page fragment
 * Contains sleep timer display
 */
public class SleepFragment extends Fragment {

    private MainActivity activity;
    private Context context;
    private View rootView;
    private boolean active = false;

    private Button sleepTimer;
    private Button resetTimer;
    private String previousSetting;

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
        active = true;

        initializeSleepTimer();
        initializeResetButton();

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
        sleepTimer.setText(convertTime(Integer.parseInt(getDurationPreference()) * 60));
        previousSetting = getDurationPreference();

        sleepTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.flipCountDown();
            }
        });

        // update sleep timer display every half second (for consistency)
        final Handler sleepTimerHandler = new Handler();
        final Runnable seekTimerRunnable = new Runnable() {
            public void run() {
                if(activity.getCountDown()) {
                    sleepTimer.setText(convertTime(activity.getSleepTimer()));
                }
                sleepTimerHandler.postDelayed(this, 500);
            }
        };
        sleepTimerHandler.postDelayed(seekTimerRunnable, 500);
    }

    /**
     * Updates the sleep timer display
     */
    private void updateSleepTimer() {
        if(previousSetting != getDurationPreference() && !activity.getCountDown()) {
            activity.setSleepTimer(Integer.parseInt(getDurationPreference()) * 60);
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
                activity.setSleepTimer(Integer.parseInt(getDurationPreference()) * 60);
                activity.setCountDown(false);
                sleepTimer.setText(convertTime(Integer.parseInt(getDurationPreference()) * 60));
            }
        });
    }

    /**
     * Gets the sleep timer duration specified in settings
     * @return String containing the current sleep timer duration setting in minutes
     */
    private String getDurationPreference() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString("sleep_timer_time_key", "1200");
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
