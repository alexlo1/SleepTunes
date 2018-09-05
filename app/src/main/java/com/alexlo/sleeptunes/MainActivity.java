package com.alexlo.sleeptunes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private boolean active = false;

    private Fragment currentFragment;
    private MainFragment mainFragment;
    private SleepFragment sleepFragment;
    public SettingsFragment settingsFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    private static int[] files = {R.raw.test1, R.raw.test2, R.raw.test3};
    public MediaController mediaPlayer;

    private SeekBar seekbar;
    private boolean seeking = false;
    private TextView mediaCurrentTime;
    private TextView mediaTotalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_main);

        active = true;

        setupNavigationView();

        initializePlayer();
        initializeControls();
        initializeSeekBar();
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        selectFragment(menu.getItem(0));
    }

    private void setupNavigationView() {
        mainFragment = new MainFragment();
        sleepFragment = new SleepFragment();
        settingsFragment = new SettingsFragment();

        fragmentManager = getFragmentManager();
        ft = fragmentManager.beginTransaction();
        ft.add(R.id.container, mainFragment);
        ft.add(R.id.container, sleepFragment);
        ft.add(R.id.container, settingsFragment);
        ft.hide(mainFragment);
        ft.hide(sleepFragment);
        ft.hide(settingsFragment);
        ft.commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();

        currentFragment = mainFragment;
        selectFragment(menu.getItem(0));
        ft = fragmentManager.beginTransaction();
        ft.show(mainFragment);
        ft.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                public boolean onNavigationItemSelected(MenuItem item) {
                    selectFragment(item);
                    return false;
                }
            });

    }

    private void selectFragment(MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.menu_main:
                pushFragment(mainFragment);
                break;
            case R.id.menu_sleep:
                pushFragment(sleepFragment);
                break;
            case R.id.menu_settings:
                pushFragment(settingsFragment);
                break;
        }
    }

    private void pushFragment(Fragment fragment) {
        if(fragment != currentFragment) {
            ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.hide(currentFragment);
            ft.show(fragment);
            ft.commit();
            currentFragment = fragment;
        }
    }

    private void initializePlayer() {
        mediaPlayer = new RawMediaController(context, files);
    }



    private void initializeControls() {

        final ToggleButton playButton = findViewById(R.id.playButton);
        final Button nextButton = findViewById(R.id.nextButton);
        final Button previousButton = findViewById(R.id.previousButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playButton.isChecked()) {
                    mediaPlayer.play();
                } else {
                    mediaPlayer.pause();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.next();

                if(playButton.isChecked()) {
                    mediaPlayer.play();
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.previous();

                if(playButton.isChecked()) {
                    mediaPlayer.play();
                }
            }
        });
    }

    private void initializeSeekBar() {
        seekbar = findViewById(R.id.seekBar);
        mediaCurrentTime = findViewById(R.id.currentTime);
        mediaTotalTime = findViewById(R.id.totalTime);

        seekbar.setMax(mediaPlayer.getDuration());
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
                    mediaPlayer.seekTo(userSelectedPosition);
                }
            });

        final Handler seekBarHandler = new Handler();
        final Runnable seekBarRunnable = new Runnable() {
            public void run() {
                if(active) {
                    int mpTime = mediaPlayer.getTime();
                    String mpTimeSec = String.valueOf(mpTime % 60);
                    String mpTimeMin = String.valueOf(mpTime / 60);
                    if (mpTimeSec.length() == 1) mpTimeSec = '0' + mpTimeSec;

                    int mpDuration = mediaPlayer.getDuration();
                    String mpDurationSec = String.valueOf(mpDuration % 60);
                    String mpDurationMin = String.valueOf(mpDuration / 60);
                    if (mpDurationSec.length() == 1) mpDurationSec = '0' + mpDurationSec;

                    if (!seeking) {
                        seekbar.setProgress(mpTime);
                        mediaCurrentTime.setText(getString(R.string.time_string, mpTimeMin, mpTimeSec));
                        mediaTotalTime.setText(getString(R.string.time_string, mpDurationMin, mpDurationSec));
                    }
                    seekBarHandler.postDelayed(this, 1000);
                }
            }
        };
        seekBarHandler.postDelayed(seekBarRunnable, 1000);
    }
}
