package com.tokopedia.inbox.rescenter.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.rescenter.player.customview.CustomVideoView;
import com.tokopedia.inbox.rescenter.player.listener.VideoPlayerView;
import com.tokopedia.inbox.rescenter.player.presenter.VideoPlayerImpl;
import com.tokopedia.inbox.rescenter.player.presenter.VideoPlayerPresenter;

import butterknife.BindView;

/**
 * Created by hangnadi on 2/9/17.
 */

public class VideoPlayerActivity extends BasePresenterActivity<VideoPlayerPresenter>
        implements VideoPlayerView, MediaPlayer.OnPreparedListener {

    public static final String PARAMS_URL_VIDEO = "url_video";

    private Uri videoUri;

    @BindView(R2.id.myVideo)
    CustomVideoView videoView;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        String stringUrl = extras.getString(PARAMS_URL_VIDEO);
        videoUri = Uri.parse(stringUrl);
    }

    @Override
    protected void initialPresenter() {
        presenter = new VideoPlayerImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_player;
    }

    @Override
    protected void initView() {
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(videoView);
        videoView.setMediaController(vidControl);
        videoView.setVideoURI(videoUri);
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Toast.makeText(getContext(), getString(R.string.error_unknown), Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        Toast.makeText(getContext(), getString(R.string.default_request_error_internal_server), Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    default:
                        Toast.makeText(getContext(), R.string.default_request_error_timeout, Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                }
            }
        });
        videoView.setOnPreparedListener(this);
    }

    private Context getContext() {
        return this;
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_VIDEO_PLAYER;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        resizeVideo(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
        mediaPlayer.start();
    }

    private void resizeVideo(int mVideoWidth, int mVideoHeight) {
        int videoWidth = mVideoWidth;
        int videoHeight = mVideoHeight;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        float heightRatio = (float) videoHeight
                / (float) displaymetrics.widthPixels;
        float widthRatio = (float) videoWidth
                / (float) displaymetrics.heightPixels;

        if (videoWidth > videoHeight) {
            Log.d("hangnadi1", "w:" + videoWidth + " > h: " + videoHeight);
            videoWidth = (int) Math.ceil((float) videoWidth * widthRatio);
            videoHeight = (int) Math.ceil((float) videoHeight * widthRatio);
            Log.d("hangnadi2", "w:" + videoWidth + " > h: " + videoHeight);
        } else {
            Log.d("hangnadi3", "w:" + videoWidth + " < h: " + videoHeight);
            videoWidth = (int) Math.ceil((float) videoWidth * heightRatio);
            videoHeight = (int) Math.ceil((float) videoHeight * heightRatio);
            Log.d("hangnadi4", "w:" + videoWidth + " > h: " + videoHeight);
        }

        videoView.setSize(videoWidth, videoHeight);
        videoView.getHolder().setFixedSize(videoWidth, videoHeight);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

}
