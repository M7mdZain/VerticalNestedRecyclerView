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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public void isOuterScrollingDown(boolean scrollDown, int value, MotionEvent event) {
        if (currentPosition >= 0 && scrollDown) {
            boolean isLastItemShown = currentLastItem == mMonths.get(currentPosition).dayCount;
            Log.d(LOG_TAG, "onTouch: isLastItemShown: " + isLastItemShown);
            enableOuterScroll(isLastItemShown, 0);

        } else {
            boolean isFirstItemShown = currentFirstItem == 1;
            Log.d(LOG_TAG, "onTouch: isFirstItemShown: " + isFirstItemShown);
            enableOuterScroll(isFirstItemShown, 0);
        }

        if (currentRV != null && event != null)
            currentRV.smoothScrollBy(0, 30 * value);
    }

    void enableOuterScroll(Boolean isEnabled, int dy) {
        if (onInnerEdgeItemShownListener != null)
            onInnerEdgeItemShownListener.enableScroll(isEnabled, dy);
    }

    class OuterViewHolder extends RecyclerView.ViewHolder {

        public static final int SPEED_FACTOR = 10000;
        private final TextView tvMonth;
        private final RecyclerView innerRecyclerView;
        private final InnerRecyclerAdapter innerRecyclerAdapter;
        private int actionDownFirstVisibleItem;
        private long actionDownTimeStamp;
        private ArrayList<Float> dyList = new ArrayList<>();

        @SuppressLint("ClickableViewAccessibility")
        OuterViewHolder(@NonNull View listItem) {
            super(listItem);
            tvMonth = listItem.findViewById(R.id.tv_month);

            // Setting up the inner RecyclerView
            innerRecyclerView = listItem.findViewById(R.id.recycler_view_inner);
            innerRecyclerView.setLayoutManager(new LinearLayoutManager(listItem.getContext()));
            innerRecyclerAdapter = new InnerRecyclerAdapter();
            KotlinSpaceKt.enforceSingleScrollDirection(innerRecyclerView);
            innerRecyclerView.setAdapter(innerRecyclerAdapter);

            AtomicBoolean isAlreadyLast = new AtomicBoolean(false);
            AtomicBoolean isAlreadyFirst = new AtomicBoolean(false);

            innerRecyclerView.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    currentLastItem = ((LinearLayoutManager) innerRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() + 1;
                    currentFirstItem = ((LinearLayoutManager) innerRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() + 1;
                    currentPosition = getAdapterPosition();
                    currentRV = innerRecyclerView;

                    int lastItem = ((LinearLayoutManager) innerRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() + 1;
                    if (lastItem == mMonths.get(currentPosition).dayCount) {
                        isAlreadyLast.set(true);
                    }
                    int firstItem = ((LinearLayoutManager) innerRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() + 1;
                    if (firstItem == 1) {
                        isAlreadyFirst.set(true);
                    }
                    actionDownFirstVisibleItem = firstItem;
                    actionDownTimeStamp = System.currentTimeMillis();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isAlreadyLast.set(false);
                    isAlreadyFirst.set(false);
                    currentRV = null;
                    dyList.clear();
                }
                dyList.add(event.getY());
                return false;
            });


            innerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                    currentLastItem = ((LinearLayoutManager) innerRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() + 1;
                    currentFirstItem = ((LinearLayoutManager) innerRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() + 1;
                    currentPosition = getAdapterPosition();
                    Log.d(LOG_TAG, "onScrollStateChanged: first: " + currentFirstItem);
                    Log.d(LOG_TAG, "onScrollStateChanged: last: " + currentLastItem);
                    float speed = SPEED_FACTOR * ((float) actionDownFirstVisibleItem - currentFirstItem) / (actionDownTimeStamp - System.currentTimeMillis());
                    Log.d(LOG_TAG, "onScrollStateChanged: speed: " + speed);

                    if (!recyclerView.canScrollVertically(1) // Is it not possible to scroll more to bottom (i.e. Last item shown)
                            && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (currentLastItem == mMonths.get(currentPosition).dayCount && !dyList.isEmpty() && !isAlreadyLast.get()) {
                            enableOuterScroll(true, (int) (speed));
                            isAlreadyLast.set(false);
                        } else
                            enableOuterScroll(true, 0);

                    } else if (!recyclerView.canScrollVertically(-1) // Is it possible to scroll more to top (i.e. First item shown)
                            && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (currentFirstItem == 1 && !dyList.isEmpty() && !isAlreadyFirst.get()) {
                            enableOuterScroll(true, (int) speed);
                            isAlreadyFirst.set(false);
                        } else
                            enableOuterScroll(true, 0);
                    }
                }
            });

        }

    }
}