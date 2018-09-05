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

public class SleepFragment extends Fragment {


    private MainActivity activity;
    private Context context;
    private View rootView;
    private boolean active = false;

    private Button sleepTimer;
    private Button resetTimer;
    private String previousSetting;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onHiddenChanged(boolean hidden) {
        if(!hidden) updateSleepTimer();
    }

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

    private void updateSleepTimer() {
        if(previousSetting != getDurationPreference() && !activity.getCountDown()) {
            activity.setSleepTimer(Integer.parseInt(getDurationPreference()) * 60);
            sleepTimer.setText(convertTime(activity.getSleepTimer()));
            previousSetting = getDurationPreference();
        }
    }

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

    private String getDurationPreference() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString("sleep_timer_time_key", "1200");
    }

    private String convertTime(int t) {
        String sec = String.valueOf(t % 60);
        String min = String.valueOf(t / 60);
        if (sec.length() == 1) sec = '0' + sec;
        if (min.length() == 1) min = '0' + min;
        return getString(R.string.time_string, min, sec);
    }
}
