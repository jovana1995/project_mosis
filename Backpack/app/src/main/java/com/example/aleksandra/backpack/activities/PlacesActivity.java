package com.example.aleksandra.backpack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.Models.FirebaseAccess;
import com.example.aleksandra.backpack.Models.PersonModel;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.CommentsAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlacesActivity extends AppCompatActivity {

    private CommentsAdapter adapter;
    private List<PersonModel> commentsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        if(getIntent().hasExtra("position"))
        {
            int i=getIntent().getIntExtra("position",0);
            TextView placeName=(TextView) findViewById(R.id.tw_place_name);
            placeName.setText(FirebaseAccess.getInstance().getPlace(i).placeName);
        }
        testData();
    }

    private void testData() {
        PersonModel cm = new PersonModel("Rusland Irkutsk", "The Netherlands", "I've visited this restaurant with my felow travelers from the hostel and I loved it! If I ever come to Spain again I will come here for sure...","10");
        commentsList.add(cm);
        PersonModel cm2 = new PersonModel("John Rose", "USA", "The ambient is very interesting and the music is great. But I didn't like my dish, it was too hot. I even started crying! :/ ","10");
        commentsList.add(cm2);
        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 16) {
            if (resultCode == RESULT_OK) {
                PersonModel cm2 = new PersonModel(data.getStringExtra("name"), data.getStringExtra("state"), data.getStringExtra("comment"), data.getStringExtra("points"));
                commentsList.add(cm2);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_comments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackpackApplication.getAppContext()));
        adapter = new CommentsAdapter(commentsList);
        recyclerView.setAdapter(adapter);

        ImageView add = findViewById(R.id.iw_places_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BackpackApplication.getAppContext(), NewCommentActivity.class);
                startActivityForResult(i, 16);
            }
        });

        Button btn = findViewById(R.id.bt_gallery);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BackpackApplication.getAppContext(), GalleryActivity.class);
                startActivity(i);
            }
        });

        Button btn2 = findViewById(R.id.bt_events);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BackpackApplication.getAppContext(), EventsActivity.class);
                startActivity(i);
            }
        });
    }

    public void showComments(List<PersonModel> comments) {
        this.commentsList = comments;
        initViews();
    }
}
