package com.alexlo.sleeptunes;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class VideoListFragment extends Fragment {

    private Activity activity;
    private Context context;
    private VideoViewModel videoViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        context = activity.getApplicationContext();RecyclerView recyclerView = activity.findViewById(R.id.recyclerview);
        final VideoListAdapter adapter = new VideoListAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        videoViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);
        videoViewModel.getAllWords().observe(this, new Observer<List<VideoId>>() {
            @Override
            public void onChanged(@Nullable final List<VideoId> ids) {
                adapter.setIds(ids);
            }
        });

        return inflater.inflate(R.layout.fragment_video_list, container, false);
    }

    public void onHiddenChanged(boolean hidden) {

    }

}
