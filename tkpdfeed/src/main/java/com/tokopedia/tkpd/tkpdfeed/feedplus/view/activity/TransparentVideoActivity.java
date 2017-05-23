package com.tokopedia.tkpd.tkpdfeed.feedplus.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.tkpd.tkpdfeed.R;

/**
 * @author by nisie on 5/22/17.
 */

public class TransparentVideoActivity extends AppCompatActivity {


    private static final String PARAM_VIDEO_URL = "PARAM_VIDEO_URL";
    private static final String PARAM_TEXT = "PARAM_TEXT";

    VideoView videoPlayer;
    TextView subtitle;

    public static Intent getIntent(Activity activity, String videoUrl, String text) {
        Intent intent = new Intent(activity, TransparentVideoActivity.class);
        intent.putExtra(PARAM_VIDEO_URL, videoUrl);
        intent.putExtra(PARAM_TEXT, text);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        setContentView(R.layout.activity_feed_video);
        videoPlayer = (VideoView) findViewById(R.id.video_player);
        subtitle = (TextView) findViewById(R.id.subtitle);

        if (getIntent().getExtras() != null
                && !getIntent().getExtras().getString(PARAM_VIDEO_URL, "").equals("")) {
            setVideo();
            subtitle.setText(getIntent().getExtras().getString(PARAM_TEXT, ""));

        } else {
            finish();
        }
    }

    private void setVideo() {
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(videoPlayer);
        videoPlayer.setMediaController(vidControl);
        videoPlayer.setVideoURI(Uri.parse(getIntent()
                .getExtras().getString(PARAM_VIDEO_URL, "")));
        videoPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Toast.makeText(TransparentVideoActivity.this,
                                getString(R.string.error_unknown),
                                Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        Toast.makeText(TransparentVideoActivity.this,
                                getString(R.string.default_request_error_internal_server),
                                Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    default:
                        Toast.makeText(TransparentVideoActivity.this,
                                R.string.default_request_error_timeout,
                                Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                }
            }
        });
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });

    }

}
