package com.alexlo.sleeptunes;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
    private TextView currentTime;
    private TextView totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_main);

        initializePlayer();
        initializeUI();
        initializeSeekBar();

    }

    private void initializePlayer() {
        mediaPlayer = new RawMediaController(context, files);
        mediaPlayer.play();
    }

    private void initializeUI() {
        final TextView nowPlaying = findViewById(R.id.nowPlaying);
        nowPlaying.setText(getString(R.string.now_playing_text, "test1"));

        final ToggleButton playButton = findViewById(R.id.playButton);
        final Button nextButton = findViewById(R.id.nextButton);
        final Button previousButton = findViewById(R.id.previousButton);
        seekbar = findViewById(R.id.seekBar);
        currentTime = findViewById(R.id.currentTime);
        totalTime = findViewById(R.id.totalTime);

        playButton.setChecked(true);

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
                        currentTime.setText(getString(R.string.time_string, mpTimeMin, mpTimeSec));
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    seeking = false;
                    mediaPlayer.seekTo(userSelectedPosition);
                }
            });

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
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
                    currentTime.setText(getString(R.string.time_string, mpTimeMin, mpTimeSec));
                    totalTime.setText(getString(R.string.time_string, mpDurationMin, mpDurationSec));
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1000);
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
