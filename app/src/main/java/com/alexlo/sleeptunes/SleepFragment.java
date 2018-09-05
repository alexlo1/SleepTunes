package com.alexlo.sleeptunes;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SleepFragment extends Fragment {


    private MainActivity activity;
    private Context context;
    private View rootView;
    private boolean active = false;

    private Button sleepTimer;
    private boolean countingDown = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sleep, container, false);
        activity = (MainActivity) getActivity();
        context = getActivity().getApplicationContext();
        active = true;

        initializeSleepTimer();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            initializeSleepTimer();
            Log.d("CHEESE", "ack");
        } else {
            Log.d("CHEESE", "flack");
        }
    }

    private void initializeSleepTimer() {
        sleepTimer = rootView.findViewById(R.id.sleepTimer);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String sleepTimerTime = sharedPref.getString("sleep_timer_time_key", "20:00");
        sleepTimer.setText(sleepTimerTime);

        sleepTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countingDown = !countingDown;
            }
        });

        final Handler sleepTimerHandler = new Handler();
        final Runnable seekTimerRunnable = new Runnable() {
            public void run() {
                if(countingDown) {
                    String sleepTime = sleepTimer.getText().toString();
                    int tempSec = Integer.parseInt(sleepTime.substring(3));
                    int tempMin = Integer.parseInt(sleepTime.substring(0, 2));

                    if (tempSec == 0 && tempMin == 0) {
                        sleep();
                    }

                    tempSec--;
                    if (tempSec < 0) {
                        tempSec = 59;
                        tempMin--;
                    }

                    String sleepSec = String.valueOf(tempSec);
                    String sleepMin = String.valueOf(tempMin);
                    if (sleepSec.length() == 1) sleepSec = '0' + sleepSec;
                    if (sleepMin.length() == 1) sleepMin = '0' + sleepMin;

                    sleepTimer.setText(getString(R.string.time_string, sleepMin, sleepSec));
                }
                sleepTimerHandler.postDelayed(this, 1000);
            }
        };
        sleepTimerHandler.postDelayed(seekTimerRunnable, 1000);
    }

    private void sleep() {
        getActivity().finishAffinity();
        System.exit(0);
    }
}
