package com.alexlo.sleeptunes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    private VideoViewModel videoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final VideoListAdapter adapter = new VideoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        videoViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        videoViewModel.getAllWords().observe(this, new Observer<List<VideoId>>() {
            @Override
            public void onChanged(@Nullable final List<VideoId> ids) {
                adapter.setIds(ids);
            }
        });
    }

//    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
//            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
//            mWordViewModel.insert(word);
//        } else {
//            Toast.makeText(
//                    getApplicationContext(),
//                    R.string.empty_not_saved,
//                    Toast.LENGTH_LONG).show();
//        }
//    }
}
