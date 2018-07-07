package com.example.aleksandra.backpack.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.GalleryAdapter;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private List<Uri> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        testData();
    }

    private void testData() {
        list.add(Uri.parse("https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg"));
        list.add(Uri.parse("https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg"));
        initViews();
    }

    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_pictures);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        GalleryAdapter adapter = new GalleryAdapter(list);
        recyclerView.setAdapter(adapter);
    }
}
