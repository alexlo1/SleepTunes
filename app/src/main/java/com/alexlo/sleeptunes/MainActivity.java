package com.alexlo.sleeptunes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private Fragment currentFragment;
    private MediaPlayerFragment mediaPlayerFragment;
    private MainFragment mainFragment;
    private SleepFragment sleepFragment;
    private SettingsFragment settingsFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    private static int[] files = {R.raw.test1, R.raw.test2, R.raw.test3};
    public MediaController mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_main);

        setupNavigationView();

        initializePlayer();
    }

    private void setupNavigationView() {
        mediaPlayerFragment = new MediaPlayerFragment();
        mainFragment = new MainFragment();
        sleepFragment = new SleepFragment();
        settingsFragment = new SettingsFragment();

        fragmentManager = getFragmentManager();
        ft = fragmentManager.beginTransaction();
        ft.add(R.id.container, mainFragment);
        ft.add(R.id.container, sleepFragment);
        ft.add(R.id.container, settingsFragment);
        ft.add(R.id.media_player_container, mediaPlayerFragment);
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
