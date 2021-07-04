package com.example.android.verticalnestedrecyclerview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up the outer RecyclerView
        OuterRecyclerAdapter outerAdapter = new OuterRecyclerAdapter(createMonths());
        outerRecyclerView = findViewById(R.id.recycler_view_outer);
        outerRecyclerView.setLayoutManager(new CustomLinearLayoutManager(this));
        outerRecyclerView.setHasFixedSize(true);
        outerRecyclerView.setAdapter(outerAdapter);

        outerAdapter.setOnInnerEdgeItemShownListener(this::enableScroll);
        outerAdapter.setOnScrollListener(y -> outerRecyclerView.smoothScrollBy(0, y));

        outerRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //scrolled to BOTTOM
                    outerAdapter.isOuterScrollingDown(true, dy);
                else if (dy < 0) //scrolled to TOP
                    outerAdapter.isOuterScrollingDown(false, dy);
            }
        });

    }

    void enableScroll(Boolean isEnabled) {
        ((CustomLinearLayoutManager) outerRecyclerView.getLayoutManager()).setScrollEnabled(isEnabled);
    }

    List<Month> createMonths() {
        List<Month> months = new ArrayList<>();
        months.add(new Month("January", 31));
        months.add(new Month("February", 1000));
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