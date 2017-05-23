package com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.tkpd.tkpdfeed.R;

/**
 * @author by nisie on 5/22/17.
 */

public class TransparentVideoActivity extends AppCompatActivity {


    private static final String PARAM_VIDEO_URL = "PARAM_VIDEO_URL";

    public static Intent getIntent(Activity activity, String videoUrl) {
        Intent intent = new Intent(activity, TransparentVideoActivity.class);
        intent.putExtra(PARAM_VIDEO_URL, videoUrl);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_video);
    }


}
