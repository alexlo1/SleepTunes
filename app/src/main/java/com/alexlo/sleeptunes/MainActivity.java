package com.alexlo.sleeptunes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private static int[] files = {R.raw.test1, R.raw.test2, R.raw.test3};

    private Context context;
    private MediaController mediaPlayer;
    private SeekBar seekbar;
    private boolean seeking = false;

    private TextView mediaCurrentTime;
    private TextView mediaTotalTime;
    private Button sleepTimer;
    private boolean countingDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_main);

        initializePlayer();
        initializeUI();
        initializeSeekBar();
        initializeSleepTimer();

        setupNavigationView();
    }

    private void initializePlayer() {
        mediaPlayer = new RawMediaController(context, files);
    }

    private void initializeUI() {
        final TextView nowPlaying = findViewById(R.id.nowPlaying);
        nowPlaying.setText(getString(R.string.now_playing_text, "test1"));

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
                nowPlaying.setText(getString(R.string.now_playing_text, "test"+String.valueOf(mediaPlayer.getCurrentFile()+1)));

                if(playButton.isChecked()) {
                    mediaPlayer.play();
                }
                initializeSeekBar();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.previous();
                nowPlaying.setText(getString(R.string.now_playing_text, "test"+String.valueOf(mediaPlayer.getCurrentFile()+1)));

                if(playButton.isChecked()) {
                    mediaPlayer.play();
                }
                initializeSeekBar();
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
                int mpTime = mediaPlayer.getTime();
                String mpTimeSec = String.valueOf(mpTime % 60);
                String mpTimeMin = String.valueOf(mpTime / 60);
                if(mpTimeSec.length() == 1) mpTimeSec = '0'+ mpTimeSec;

                int mpDuration = mediaPlayer.getDuration();
                String mpDurationSec = String.valueOf(mpDuration % 60);
                String mpDurationMin = String.valueOf(mpDuration / 60);
                if(mpDurationSec.length() == 1) mpDurationSec = '0'+ mpDurationSec;

                if(!seeking) {
                    seekbar.setProgress(mpTime);
                    mediaCurrentTime.setText(getString(R.string.time_string, mpTimeMin, mpTimeSec));
                    mediaTotalTime.setText(getString(R.string.time_string, mpDurationMin, mpDurationSec));
                }
                seekBarHandler.postDelayed(this, 1000);
            }
        };
        seekBarHandler.postDelayed(seekBarRunnable, 1000);
    }

    private void initializeSleepTimer() {
        sleepTimer = findViewById(R.id.sleepTimer);

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
        finishAffinity();
        System.exit(0);
    }

    private void setupNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            Menu menu = bottomNavigationView.getMenu();
            selectFragment(menu.getItem(0));

            bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectFragment(item);
                        return false;
                    }
                });
        }
    }

    private void selectFragment(MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.menu_main:
                pushFragment(new MainFragment());
                break;
            case R.id.menu_sleep:
                pushFragment(new SleepFragment());
                break;
            case R.id.menu_settings:
                pushFragment(new SettingsFragment());
                break;
        }
    }

    private void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft.replace(R.id.container, fragment);
                ft.commit();
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
