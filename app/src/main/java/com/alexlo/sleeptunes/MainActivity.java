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
    private MainFragment mainFragment;
    private SleepFragment sleepFragment;
    private SettingsFragment settingsFragment;

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
        mainFragment = new MainFragment();
        sleepFragment = new SleepFragment();
        settingsFragment = new SettingsFragment();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.container, mainFragment);
        ft.add(R.id.container, sleepFragment);
        ft.add(R.id.container, settingsFragment);
        ft.hide(sleepFragment);
        ft.hide(settingsFragment);
        ft.commit();
        currentFragment = mainFragment;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView != null) {
            Menu menu = bottomNavigationView.getMenu();

            //selectFragment(menu.getItem(0));

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
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                ft.hide(currentFragment);
                ft.show(fragment);
                ft.commit();
                currentFragment = fragment;
            }
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
