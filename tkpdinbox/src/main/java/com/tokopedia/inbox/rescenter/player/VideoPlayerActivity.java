package com.tokopedia.inbox.rescenter.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.MediaController;
import android.widget.Toast;
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
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Toast.makeText(getContext(), getString(R.string.error_unknown), Toast.LENGTH_SHORT);
                        finish();
                        return true;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        Toast.makeText(getContext(), getString(R.string.default_request_error_internal_server), Toast.LENGTH_SHORT);
                        finish();
                        return true;
                }
                return false;
            }
        });
        videoView.start();
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
}
