package com.example.aleksandra.backpack.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.EventsAdapter;
import com.example.aleksandra.backpack.models.EventModel;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    private List<EventModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        testData();
    }

    private void testData() {
        list.add(new EventModel("7pm", "In front of the restaurant", "https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg", "I am going to this restaurant with two friends from hostel to try traditional thai food. Feel free to join us!", "Anne Smith", "England","a","a","a"));
        list.add(new EventModel("7pm", "In front of the restaurant", "https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg", "I am going to this restaurant with two friends from hostel to try traditional thai food. Feel free to join us!", "Anne Smith", "England","a","a","a"));
        list.add(new EventModel("7pm", "In front of the restaurant", "https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg", "I am going to this restaurant with two friends from hostel to try traditional thai food. Feel free to join us!", "Anne Smith", "England","a","a","a"));
        initViews();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_events);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        adapter = new EventsAdapter(list);
        recyclerView.setAdapter(adapter);
    }
}
