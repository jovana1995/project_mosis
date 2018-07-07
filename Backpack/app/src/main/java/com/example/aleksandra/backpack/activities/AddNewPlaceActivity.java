package com.example.aleksandra.backpack.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.aleksandra.backpack.BackpackApplication;
import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.adapters.GalleryAdapter;
import com.example.aleksandra.backpack.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddNewPlaceActivity extends AppCompatActivity {
    private List<Uri> list = new ArrayList<>();
    private Uri captureImageUri;
    private static final int RESULT_LOAD_IMAGE = 777;
    private GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

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
        adapter = new GalleryAdapter(list);
        recyclerView.setAdapter(adapter);

        Button btnAddEvent = findViewById(R.id.bt_new_place_add);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final Button btnCancel = findViewById(R.id.bt_new_place_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnLocation = findViewById(R.id.bt_new_place_location);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button btnPicture = findViewById(R.id.bt_new_place_pictures);
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(btnCancel);
                getPickImageIntent();
            }
        });
    }

    public void closeKeyboard(View v) {                                                                  //view should be a button or radiobutton or checkbox or layout
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert mgr != null;
        mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void getPickImageIntent() {
        // start camera or gallery
        captureImageUri = uploadingImageSetup();
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri);
        Intent pickerIntent = new Intent();
        pickerIntent.setType("image/*");
        pickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        // chooser intent
        Intent chooserIntent = Intent.createChooser(captureIntent, this.getString(R.string.title_image_picker));
        // add camera intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{pickerIntent});
        this.startActivityForResult(chooserIntent, RESULT_LOAD_IMAGE);
    }

    public Uri uploadingImageSetup() {
        final String path = android.os.Environment.DIRECTORY_DCIM;
        String fname = "img_" + System.currentTimeMillis() + ".jpg";
        File photo = new File(Environment.getExternalStorageDirectory(), path + fname);
        return Uri.fromFile(photo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri;
            if (data == null || MediaStore.ACTION_IMAGE_CAPTURE.equals(data.getAction())) {
                imageUri = captureImageUri;
            } else {
                imageUri = data.getData();
            }
            list.add(imageUri);
            adapter.notifyDataSetChanged();
        }
    }
}
