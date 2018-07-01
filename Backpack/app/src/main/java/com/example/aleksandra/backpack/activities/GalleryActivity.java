package com.example.aleksandra.backpack.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.CommentModel;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.CommentsAdapter;
import com.example.aleksandra.backpack.adapters.GalleryAdapter;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private List<Uri> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        testData();
    }

    private void testData() {
        list.add(Uri.parse("https://secure.i.telegraph.co.uk/multimedia/archive/03597/POTD_chick_3597497k.jpg"));
        list.add(Uri.parse("https://secure.i.telegraph.co.uk/multimedia/archive/03597/POTD_chick_3597497k.jpg"));
        initViews();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_pictures);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        adapter = new GalleryAdapter(list);
        recyclerView.setAdapter(adapter);
    }
}
