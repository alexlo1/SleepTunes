package com.alexlo.sleeptunes;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MediaPlayerFragment extends Fragment {

    private MainActivity activity;
    private Context context;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_media_player, container, false);
        activity = (MainActivity) getActivity();
        context = getActivity().getApplicationContext();

        return rootView;
    }


}
