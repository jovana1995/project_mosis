package com.example.aleksandra.backpack.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.CommentsAdapter;
import com.example.aleksandra.backpack.adapters.ProfileCommentAdapter;
import com.example.aleksandra.backpack.adapters.ProfileEventAdapter;
import com.example.aleksandra.backpack.adapters.ProfileNewPlaceAdapter;
import com.example.aleksandra.backpack.models.CommentModel;
import com.example.aleksandra.backpack.models.EventModel;
import com.example.aleksandra.backpack.models.PlaceModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private List<CommentModel> commentsList = new ArrayList<>();
    private List<EventModel> eventList = new ArrayList<>();
    private List<PlaceModel> placeList = new ArrayList<>();

    private ImageView ivAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        testData();
    }

    public void testData() {
        commentsList.add(new CommentModel("sdhybcujdbcsvhbdsivkudsbvikbwik", "MyPlace", "Golubarska 13, Nis", "2h ago"));
        eventList.add(new EventModel("8pm", "In front of the restaurant", "https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg", "dhbcuvwu ib  b biwb iwb", "Anne Smith", "England", "Sea Thai Restaurant", "la rue sue", "2w ago"));
        placeList.add(new PlaceModel("vhjsbvjs", "Thai restaurant", "Rue la sue", "England", "+2222 2 2 2 222", "https://lh6.googleusercontent.com/-uOsWbD_YAVo/AAAAAAAAAAI/AAAAAAAAATc/tbmd6HaYp34/photo.jpg", "2w ago"));
        initViews();
    }

    private void initViews() {
        RecyclerView recyclerViewEvents = findViewById(R.id.recycler_view_profile_events);
        recyclerViewEvents.setHasFixedSize(true);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        ProfileEventAdapter adapterEvents = new ProfileEventAdapter(eventList);
        recyclerViewEvents.setAdapter(adapterEvents);

        RecyclerView recyclerViewComments = findViewById(R.id.recycler_view_profile_comments);
        recyclerViewComments.setHasFixedSize(true);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        ProfileCommentAdapter adapterComments = new ProfileCommentAdapter(commentsList);
        recyclerViewComments.setAdapter(adapterComments);

        RecyclerView recyclerViewNewPlaces = findViewById(R.id.recycler_view_profile_new_places);
        recyclerViewNewPlaces.setHasFixedSize(true);
        recyclerViewNewPlaces.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        ProfileNewPlaceAdapter adapterNewPlace = new ProfileNewPlaceAdapter(placeList);
        recyclerViewNewPlaces.setAdapter(adapterNewPlace);

        ivAdd = findViewById(R.id.iw_places_add);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(ProfileActivity.this, ivAdd);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.dropdown, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Add Event")) {
                            Intent i = new Intent(ProfileActivity.this, AddEventActivity.class);
                            startActivity(i);
                        }

                        if (item.getTitle().equals("New Place")) {
                            Intent i = new Intent(ProfileActivity.this, AddNewPlaceActivity.class);
                            startActivity(i);
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method
    }
}
