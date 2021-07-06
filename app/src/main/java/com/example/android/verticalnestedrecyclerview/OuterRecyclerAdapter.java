package com.example.android.verticalnestedrecyclerview;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.android.verticalnestedrecyclerview.MainActivity.LOG_TAG;

/*
 * By: M7md.zain@gamil.com
 * 04.July.2021
 * */
public class OuterRecyclerAdapter extends RecyclerView.Adapter<OuterRecyclerAdapter.OuterViewHolder> {

    private final List<Month> mMonths;
    private OnInnerEdgeItemShownListener onInnerEdgeItemShownListener;
    private int currentPosition = 0;
    private int currentFirstItem;
    private int currentLastItem;
    private RecyclerView currentRV;

    OuterRecyclerAdapter(List<Month> months) {
        this.mMonths = months;
    }

    @NonNull
    @Override
    public OuterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row_outer, parent, false);
        return new OuterViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull OuterViewHolder holder, int position) {
        Month month = mMonths.get(position);
        holder.tvMonth.setText(month.getName());
        holder.innerRecyclerAdapter.setDays(month.getDayCount());
    }

    @Override
    public int getItemCount() {
        return mMonths.size();
    }

    public void setOnInnerEdgeItemShownListener(OnInnerEdgeItemShownListener onInnerEdgeItemShownListener) {
        this.onInnerEdgeItemShownListener = onInnerEdgeItemShownListener;
    }

    public void isOuterScrollingDown(boolean scrollDown, int value) {
        if (scrollDown) {
            boolean isLastItemShown = currentLastItem == mMonths.get(currentPosition).dayCount;
            Log.d(LOG_TAG, "onTouch: isLastItemShown: " + isLastItemShown);
//            if (!isLastItemShown) onScrollListener.onScroll(value);
            enableOuterScroll(isLastItemShown);

        } else {
            boolean isFirstItemShown = currentFirstItem == 1;
            Log.d(LOG_TAG, "onTouch: isFirstItemShown: " + isFirstItemShown);
//            if (!isFirstItemShown) onScrollListener.onScroll(value);
            enableOuterScroll(isFirstItemShown);
        }
        if (currentRV != null)
            currentRV.smoothScrollBy(0, 30 * value);
    }

    void enableOuterScroll(Boolean isEnabled) {
        if (onInnerEdgeItemShownListener != null)
            onInnerEdgeItemShownListener.enableScroll(isEnabled);
    }

    class OuterViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvMonth;
        private final RecyclerView innerRecyclerView;
        private final InnerRecyclerAdapter innerRecyclerAdapter;

        @SuppressLint("ClickableViewAccessibility")
        OuterViewHolder(@NonNull View listItem) {
            super(listItem);
            tvMonth = listItem.findViewById(R.id.tv_month);

            // Setting up the inner RecyclerView
            innerRecyclerView = listItem.findViewById(R.id.recycler_view_inner);
            innerRecyclerView.setLayoutManager(new LinearLayoutManager(listItem.getContext()));
            innerRecyclerAdapter = new InnerRecyclerAdapter();
            innerRecyclerView.setAdapter(innerRecyclerAdapter);

            innerRecyclerView.setOnTouchListener((v, event) -> {
                currentLastItem = ((LinearLayoutManager) innerRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() + 1;
                currentFirstItem = ((LinearLayoutManager) innerRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() + 1;
                currentPosition = getAdapterPosition();
                currentRV = innerRecyclerView;
                return false;
            });

            innerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                    if (!recyclerView.canScrollVertically(1) // Is it not possible to scroll more to bottom (i.e. Last item shown)
                            && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        enableOuterScroll(true);

                    } else if (!recyclerView.canScrollVertically(-1) // Is it possible to scroll more to top (i.e. First item shown)
                            && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        enableOuterScroll(true);
                    }
                }
            });

        }

    }
}