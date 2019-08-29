package com.android.segunfrancis.devicetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DistanceAdapter extends RecyclerView.Adapter<DistanceAdapter.DistanceVIewHolder> {

    private List<Distance> mDistanceList;
    private LayoutInflater mInflater;
    private Context mContext;
    private DistanceViewModel mViewModel;

    public DistanceAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DistanceVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new DistanceVIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistanceVIewHolder holder, int position) {
        if (mDistanceList != null) {
            Distance currentDistance = mDistanceList.get(position);

        }
    }

    @Override
    public int getItemCount() {
        if (mDistanceList != null) {
            return mDistanceList.size();
        } else return 0;
    }

    public void setDistances(List<Distance> distances) {
        this.mDistanceList = distances;
        notifyDataSetChanged();
    }

    public class DistanceVIewHolder extends RecyclerView.ViewHolder {
        private TextView distanceTextView, dateTextView, timeTextView;

        public DistanceVIewHolder(@NonNull View itemView) {
            super(itemView);
            distanceTextView = itemView.findViewById(R.id.total_distance_item_text);
            dateTextView = itemView.findViewById(R.id.date_text);
            timeTextView = itemView.findViewById(R.id.time_text);
        }
    }
}
