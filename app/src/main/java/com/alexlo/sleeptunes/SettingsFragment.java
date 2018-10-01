package com.alexlo.sleeptunes;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Settings page fragment
 * Contains settings
 */
public class SettingsFragment extends PreferenceFragment {

    /**
     * Called when fragment is starting, initializes layout
     * @param savedInstanceState If non-null, previous state to reconstruct
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
