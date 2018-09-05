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

public class MainFragment extends Fragment {

    private MainActivity activity;
    private Context context;
    private View rootView;
    private boolean active = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        activity = (MainActivity) getActivity();
        context = getActivity().getApplicationContext();
        active = true;

        initializeDisplay();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void initializeDisplay() {

    }
}
