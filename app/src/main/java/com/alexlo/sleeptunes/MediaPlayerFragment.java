package com.alexlo.sleeptunes;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MediaPlayerFragment extends Fragment {

    private MainActivity activity;
    private Context context;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_media_player, container, false);
        activity = (MainActivity) getActivity();
        context = getActivity().getApplicationContext();

        initializeUI();

        return rootView;
    }


    private void initializeUI() {
        final TextView nowPlaying = rootView.findViewById(R.id.nowPlaying);
        //nowPlaying.setText(getString(R.string.now_playing_text, "test1"));

        final ToggleButton playButton = rootView.findViewById(R.id.playButton);
        final Button nextButton = rootView.findViewById(R.id.nextButton);
        final Button previousButton = rootView.findViewById(R.id.previousButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playButton.isChecked()) {
                    activity.mediaPlayer.play();
                } else {
                    activity.mediaPlayer.pause();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.mediaPlayer.next();
                //nowPlaying.setText(getString(R.string.now_playing_text, "test"+String.valueOf(activity.mediaPlayer.getCurrentFile()+1)));

                if(playButton.isChecked()) {
                    activity.mediaPlayer.play();
                }
                //initializeSeekBar();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.mediaPlayer.previous();
                //nowPlaying.setText(getString(R.string.now_playing_text, "test"+String.valueOf(activity.mediaPlayer.getCurrentFile()+1)));

                if(playButton.isChecked()) {
                    activity.mediaPlayer.play();
                }
                //initializeSeekBar();
            }
        });
    }
}
