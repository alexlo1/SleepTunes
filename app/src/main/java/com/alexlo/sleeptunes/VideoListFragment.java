package com.alexlo.sleeptunes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class VideoListFragment extends Fragment {

    private Activity activity;
    private Context context;
    private View rootView;

    private RecyclerView recyclerView;
    private VideoListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_video_list, container, false);
        activity = getActivity();
        context = activity.getApplicationContext();

        recyclerView = rootView.findViewById(R.id.recyclerview);
        adapter = new VideoListAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        return rootView;
    }

    public void onHiddenChanged(boolean hidden) {

    }

    public void setAdapter(List<VideoId> list) {
        adapter.setIds(list);
    }

}
