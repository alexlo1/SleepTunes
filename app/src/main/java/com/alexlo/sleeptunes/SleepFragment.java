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

public class SleepFragment extends Fragment {


    private MainActivity activity;
    private Context context;
    private View rootView;
    private boolean active = false;

    private SeekBar seekbar;
    private boolean seeking = false;

    private TextView mediaCurrentTime;
    private TextView mediaTotalTime;
    private Button sleepTimer;
    private boolean countingDown = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        activity = (MainActivity) getActivity();
        context = getActivity().getApplicationContext();
        active = true;

        initializeSeekBar();
        initializeSleepTimer();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initializeSeekBar() {
        seekbar = rootView.findViewById(R.id.seekBar);
        mediaCurrentTime = rootView.findViewById(R.id.currentTime);
        mediaTotalTime = rootView.findViewById(R.id.totalTime);

        seekbar.setMax(activity.mediaPlayer.getDuration());
        seekbar.setProgress(0);
        seekbar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        seeking = true;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser && seeking) {
                            userSelectedPosition = progress;

                            String mpTimeSec = String.valueOf(progress % 60);
                            String mpTimeMin = String.valueOf(progress / 60);
                            if(mpTimeSec.length() == 1) mpTimeSec = '0'+ mpTimeSec;
                            mediaCurrentTime.setText(getString(R.string.time_string, mpTimeMin, mpTimeSec));
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seeking = false;
                        activity.mediaPlayer.seekTo(userSelectedPosition);
                    }
                });

        final Handler seekBarHandler = new Handler();
        final Runnable seekBarRunnable = new Runnable() {
            public void run() {
                if(active) {
                    int mpTime = activity.mediaPlayer.getTime();
                    String mpTimeSec = String.valueOf(mpTime % 60);
                    String mpTimeMin = String.valueOf(mpTime / 60);
                    if (mpTimeSec.length() == 1) mpTimeSec = '0' + mpTimeSec;

                    int mpDuration = activity.mediaPlayer.getDuration();
                    String mpDurationSec = String.valueOf(mpDuration % 60);
                    String mpDurationMin = String.valueOf(mpDuration / 60);
                    if (mpDurationSec.length() == 1) mpDurationSec = '0' + mpDurationSec;

                    if (!seeking) {
                        seekbar.setProgress(mpTime);
                        mediaCurrentTime.setText(getString(R.string.time_string, mpTimeMin, mpTimeSec));
                        mediaTotalTime.setText(getString(R.string.time_string, mpDurationMin, mpDurationSec));
                    }
                    seekBarHandler.postDelayed(this, 500);
                }
            }
        };
        seekBarHandler.postDelayed(seekBarRunnable, 500);
    }

    private void initializeSleepTimer() {
        sleepTimer = rootView.findViewById(R.id.sleepTimer);

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
