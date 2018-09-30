package com.alexlo.sleeptunes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.WordViewHolder> {

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView videoIdItemView;

        private WordViewHolder(View itemView) {
            super(itemView);
            videoIdItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater inflater;
    private List<VideoId> videoIds; // Cached copy of words

    VideoListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        if (videoIds != null) {
            VideoId current = videoIds.get(position);
            holder.videoIdItemView.setText(current.getId());
        } else {
            // Covers the case of data not being ready yet.
            holder.videoIdItemView.setText("No Word");
        }
    }

    void setIds(List<VideoId> ids){
        videoIds = ids;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (videoIds != null)
            return videoIds.size();
        else return 0;
    }
}
