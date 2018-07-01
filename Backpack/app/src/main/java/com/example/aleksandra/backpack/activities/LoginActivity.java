package com.example.aleksandra.backpack.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aleksandra.backpack.R;
import com.example.aleksandra.backpack.utils.Utils;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    private Uri captureImageUri;
    private static final int RESULT_LOAD_IMAGE = 777;
    private ImageView circularImageView;
    private ImageView smallView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }
    public void initialize(){
        circularImageView = findViewById(R.id.imw_profile_picture);
        smallView = findViewById(R.id.imw_small_picture);
        final TextView fname = findViewById(R.id.tw_fname);

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard(fname);
                getPickImageIntent();
            }
        });
        circularImageView.setClickable(true);
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
            Utils.loadImageWithGlideCircle(imageUri, circularImageView);
            smallView.setVisibility(View.INVISIBLE);
        }
    }

    public Uri uploadingImageSetup() {
        final String path = android.os.Environment.DIRECTORY_DCIM;
        String fname = "img_" + System.currentTimeMillis() + ".jpg";
        File photo = new File(Environment.getExternalStorageDirectory(), path + fname);
        return Uri.fromFile(photo);
    }
}