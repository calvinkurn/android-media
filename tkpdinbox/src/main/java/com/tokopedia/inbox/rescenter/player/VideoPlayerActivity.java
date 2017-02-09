package com.tokopedia.inbox.rescenter.player;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.MediaController;
import android.widget.VideoView;

import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.inbox.rescenter.player.listener.VideoPlayerView;
import com.tokopedia.inbox.rescenter.player.presenter.VideoPlayerImpl;
import com.tokopedia.inbox.rescenter.player.presenter.VideoPlayerPresenter;

import butterknife.BindView;

/**
 * Created by hangnadi on 2/9/17.
 */

public class VideoPlayerActivity extends BasePresenterActivity<VideoPlayerPresenter>
        implements VideoPlayerView {

    public static final String PARAMS_URL_VIDEO = "url_video";

    private Uri videoUri;

    @BindView(R2.id.myVideo)
    VideoView videoView;

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
        videoView.start();
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
}
