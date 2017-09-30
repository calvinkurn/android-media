package com.tokopedia.seller.common.imageeditor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.tokopedia.seller.R;

public final class CropResultActivity extends Activity {

    public static final String URI = "URI";

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_crop_result);

        imageView = ((ImageView) findViewById(R.id.resultImageView));

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(URI);
        if (uri!= null) {
            imageView.setImageURI(uri);
        } else {
            Toast.makeText(this, "No image is set to show", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onImageViewClicked(View view) {
        finish();
    }
}
