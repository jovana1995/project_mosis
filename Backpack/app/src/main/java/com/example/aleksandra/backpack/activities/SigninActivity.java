package com.example.aleksandra.backpack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.aleksandra.backpack.R;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initialize();
    }

    public void initialize(){
        //Button buttonSignin = (Button) findViewById(R.id.button_signin);
        TextView textViewNoAccount = findViewById(R.id.tw_noaccount);
        textViewNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SigninActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
