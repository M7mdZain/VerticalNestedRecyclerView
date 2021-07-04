package com.example.android.verticalnestedrecyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.android.verticalnestedrecyclerview.MainActivity.LOG_TAG;

/*
 * By: M7md.zain@gamil.com
 * 04.July.2021
 * */

public class InnerRecyclerAdapter extends RecyclerView.Adapter<InnerRecyclerAdapter.InnerViewHolder> {

    private int days = 0;
    public void setDays(int days) {
        this.days = days;
    }

    // Tracking the currently loaded items in the RecyclerView
    private final ArrayList<Integer> currentLoadedPositions = new ArrayList<>();


    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row_inner, parent, false);
        return new InnerViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        holder.tvDay.setText(String.valueOf(position + 1));
        currentLoadedPositions.add(position);
        Log.d(LOG_TAG, "onViewRecycled: " + days + " " + currentLoadedPositions);
    }

    @Override
    public int getItemCount() {
        return days;
    }

    static class InnerViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay;

        InnerViewHolder(@NonNull View listItem) {
            super(listItem);
            tvDay = listItem.findViewById(R.id.tv_day);
        }
    }


    @Override
    public void onViewRecycled(@NonNull InnerViewHolder holder) {
        super.onViewRecycled(holder);
        currentLoadedPositions.remove(Integer.valueOf(holder.getAdapterPosition()));
        Log.d(LOG_TAG, "onViewRecycled: " + days + " " + currentLoadedPositions);
    }


}
