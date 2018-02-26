package com.tokopedia.trainapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.tkpdtrain.homepage.presentation.view.TrainHomepageActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, TrainHomepageActivity.class);
        startActivity(intent);
    }

}
