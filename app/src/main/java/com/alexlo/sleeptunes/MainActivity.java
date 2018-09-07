package com.alexlo.sleeptunes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Main activity class
 * App only has this one activity for simplicity, using fragments for different pages
 * Contains media controls and bottom navigation menu
 */
public class MainActivity extends AppCompatActivity {

    private Context context;
    private boolean active = false;

    private Fragment currentFragment;
    private MainFragment mainFragment;
    private SleepFragment sleepFragment;
    private SettingsFragment settingsFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    // built in music files
    private static int[] files = {R.raw.the_name_of_life, R.raw.promise_of_the_world, R.raw.path_of_the_wind, R.raw.a_town_with_an_ocean_view};
    private static String[] filenames = {"The Name of Life", "Promise of the World", "Path of the Wind", "A Town with an Ocean View"};
    public MediaController mediaPlayer;

    private SeekBar seekbar;
    private boolean seeking = false;
    private TextView mediaCurrentTime;
    private TextView mediaTotalTime;

    private boolean countingDown;
    private int sleepTimer = 1200;

    /**
     * Called when activity is starting, initializes layout
     * @param savedInstanceState If non-null, previous state to reconstruct
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_main);

        active = true;

        initializePlayer();
        initializeControls();
        initializeSeekBar();
        initializeSleepTimer();

        setupNavigationView();
    }

    /**
     * Override the back button
     * Now always displays the main fragment
     */
    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        selectFragment(menu.getItem(0));
    }

    /**
     * Initializes the bottom navigation menu
     */
    private void setupNavigationView() {
        mainFragment = new MainFragment();
        sleepFragment = new SleepFragment();
        settingsFragment = new SettingsFragment();

        // create and hide all fragments
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

        // show the main fragment to start
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

    /**
     * Convert menu item into the corresponding fragment and push it
     * @param item The navigation menu item selected
     */
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

    /**
     * Hides the current fragment page and displays the specified page
     * @param fragment The fragment to be shown
     */
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

    /**
     * Initializes the media player class
     */
    private void initializePlayer() {
        mediaPlayer = new RawMediaController(context, files, filenames);
    }

    /**
     * Initializes the media control buttons (play, pause, next, previous)
     */
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

    /**
     * Initializes the seekbar that displays the current time on the audio currently being played
     */
    private void initializeSeekBar() {
        seekbar = findViewById(R.id.seekBar);
        mediaCurrentTime = findViewById(R.id.currentTime);
        mediaTotalTime = findViewById(R.id.totalTime);

        seekbar.setMax(mediaPlayer.getDuration());
        seekbar.setProgress(0);

        // allow the seekbar to be dragged, and update the media player current time accordingly
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
                        mediaCurrentTime.setText(convertTime(progress, false));
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    seeking = false;
                    mediaPlayer.seekTo(userSelectedPosition);
                }
            });

        // update seekbar with media player progress every 0.1 seconds
        final Handler seekBarHandler = new Handler();
        final Runnable seekBarRunnable = new Runnable() {
            public void run() {
                if(active) {
                    if (!seeking) {
                        seekbar.setProgress(mediaPlayer.getTime());
                        mediaCurrentTime.setText(convertTime(mediaPlayer.getTime(), false));
                        mediaTotalTime.setText(convertTime(mediaPlayer.getDuration(), false));
                    }
                    seekBarHandler.postDelayed(this, 100);
                }
            }
        };
        seekBarHandler.postDelayed(seekBarRunnable, 100);
    }

    /**
     * Initializes the sleep timer countdown
     */
    private void initializeSleepTimer() {
        sleepTimer = getDurationPreference();

        // update the sleep timer countdown every second
        final Handler sleepTimerHandler = new Handler();
        final Runnable seekTimerRunnable = new Runnable() {
            public void run() {
                if(countingDown) {
                    if(sleepTimer == 0) sleep();
                    sleepTimer--;
                    if(getFadePreference()) {
                        volumeFade();
                    } else {
                        undoVolumeFade();
                    }
                }
                sleepTimerHandler.postDelayed(this, 1000);
            }
        };
        sleepTimerHandler.postDelayed(seekTimerRunnable, 1000);
    }

    /**
     * Quits the activity and closes the app
     */
    private void sleep() {
        finishAffinity();
        System.exit(0);
    }

    /**
     * Gradually decreases the media player volume
     */
    private void volumeFade() {
        int preference = getDurationPreference();
        double currentVolume = sleepTimer * 1.0 / preference;
        mediaPlayer.setVolume(currentVolume);
    }

    /**
     * Sets volume back to full
     */
    private void undoVolumeFade() {
        mediaPlayer.setVolume(1);
    }

    /**
     * Inverts the value of countingDown
     */
    public void flipCountDown() {
        countingDown = !countingDown;
    }

    /**
     * Mutator for countingDown
     * @param b New boolean value
     */
    public void setCountDown(boolean b) {
        countingDown = b;
    }

    /**
     * Accessor for countingDown
     * @return Boolean detailing whether or not sleep timer is counting down
     */
    public boolean getCountDown() {
        return countingDown;
    }

    /**
     * Mutator for sleepTimer
     * @param t New time
     */
    public void setSleepTimer(int t) {
        sleepTimer = t;
    }

    /**
     * Accessor for sleepTimer
     * @return Time remaining on the sleep timer, in seconds
     */
    public int getSleepTimer() {
       return sleepTimer;
    }

    /**
     * Converts an integer number of seconds into a time string
     * @param t Integer number of seconds
     * @param addMinuteZero Boolean detailing whether or not to add a 0 to x:xx, i.e. 0x:xx
     * @return String in the form {min}:{sec} (x:xx or xx:xx)
     */
    public String convertTime(int t, boolean addMinuteZero) {
        String sec = String.valueOf(t % 60);
        String min = String.valueOf(t / 60);
        if (sec.length() == 1) sec = '0' + sec;
        if (addMinuteZero && min.length() == 1) min = '0' + min;
        return getString(R.string.time_string, min, sec);
    }

    /**
     * Gets the sleep timer duration specified in settings
     * @return Integer containing the current sleep timer duration setting in seconds
     */
    private int getDurationPreference() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(sharedPref.getString("sleep_timer_time_key", "1200")) * 60;
    }

    /**
     * Gets the sleep timer duration specified in settings
     * @return Integer containing the current sleep timer duration setting in seconds
     */
    private boolean getFadePreference() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean("volume_fade_key", false);
    }
}
