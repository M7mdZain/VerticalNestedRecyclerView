package com.example.android.verticalnestedrecyclerview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
/*
 * By: M7md.zain@gamil.com
 * 04.July.2021
 * */

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "LOG_TAG";
    private RecyclerView outerRecyclerView;
    private float oldY = -1f;
    private OuterRecyclerAdapter outerAdapter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the outer RecyclerView
        outerAdapter = new OuterRecyclerAdapter(createMonths());
        outerRecyclerView = findViewById(R.id.recycler_view_outer);
        CustomLinearLayoutManager manager = new CustomLinearLayoutManager(this);
        manager.setSmoothScrollbarEnabled(true);
        outerRecyclerView.setLayoutManager(manager);
        outerRecyclerView.setHasFixedSize(true);
        outerRecyclerView.setAdapter(outerAdapter);

//        KotlinSpaceKt.enforceSingleScrollDirection(outerRecyclerView);

        outerAdapter.setOnInnerEdgeItemShownListener((isEnabled, dy) -> enableScroll(isEnabled, dy));
        outerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                Log.d(LOG_TAG, "onTouch Scroll: " + dy);
                if (dy > 0) //scrolled to BOTTOM
                    outerAdapter.isOuterScrollingDown(true, dy, null);
                else if (dy < 0) //scrolled to TOP
                    outerAdapter.isOuterScrollingDown(false, dy, null);
            }
        });


        outerRecyclerView.setOnTouchListener((v, event) -> {
            Log.d(LOG_TAG, "onTouch: ");
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    oldY = -1;
                    break;

                case MotionEvent.ACTION_MOVE:
                    float newY = event.getRawY();
                    Log.d(LOG_TAG, "onTouch: MOVE " + (oldY - newY));

                    if (oldY == -1f) {
                        oldY = newY;
                        return true; // avoid further listeners (i.e. addOnScrollListener)

                    } else if (oldY < newY) { // increases means scroll UP
                        outerAdapter.isOuterScrollingDown(false, (int) (oldY - newY), event);
                        oldY = newY;

                    } else if (oldY > newY) { // decreases means scroll DOWN
                        outerAdapter.isOuterScrollingDown(true, (int) (oldY - newY), event);
                        oldY = newY;
                    }
                    break;
            }
            return false;
        });

    }

    void enableScroll(Boolean isEnabled, int dy) {
        Log.d(LOG_TAG, "onTouch: enableScroll " + isEnabled);
        ((CustomLinearLayoutManager) outerRecyclerView.getLayoutManager()).setScrollEnabled(isEnabled);
        if (isEnabled && dy != 0)
            outerRecyclerView.smoothScrollBy(0, dy);

    }

    List<Month> createMonths() {
        List<Month> months = new ArrayList<>();
        months.add(new Month("January", 31));
        months.add(new Month("February", 28));
        months.add(new Month("March", 31));
        months.add(new Month("April", 30));
        months.add(new Month("May", 31));
        months.add(new Month("June", 30));
        months.add(new Month("July", 31));
        months.add(new Month("August", 31));
        months.add(new Month("September", 30));
        months.add(new Month("October", 31));
        months.add(new Month("November", 30));
        months.add(new Month("December", 31));
        return months;
    }
}