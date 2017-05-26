package com.tokopedia.discovery.intermediary.view.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tokopedia.core.R2;
import com.tokopedia.discovery.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alifa on 5/26/17.
 */

public class YoutubeIntermediaryActivity extends YouTubeBaseActivity {

    public static final String EXTRA_YOUTUBE_VIDEO_URL = "EXTRA_YOUTUBE_VIDEO_URL";

    @BindView(R2.id.youtube_player_main)
    YouTubePlayerView youTubePlayerView;


    private YouTubePlayer youTubePlayerScreen;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediary_youtube);
        getVideoDatas();
        ButterKnife.bind(this);
        setSideBarAvailability();

    }

    private void getVideoDatas() {
        videoUrl = getIntent().getStringExtra(EXTRA_YOUTUBE_VIDEO_URL);
    }

    private void setSideBarAvailability() {
        youTubePlayerView.initialize(getApplicationContext().getString(com.tokopedia.core.R.string.GOOGLE_API_KEY),
                onSingleVideoInitializedListener());
    }

    private YouTubePlayer.OnInitializedListener onSingleVideoInitializedListener() {
        return new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer,
                                                boolean b) {
                YoutubeIntermediaryActivity.this.youTubePlayerScreen = youTubePlayer;
                youTubePlayer.setFullscreen(true);
                youTubePlayer.setShowFullscreenButton(false);
                playYoutubeVideo(youTubePlayer, videoUrl);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult initializationResult) {
                Log.d("test", "onInitializationFailure: ");

            }
        };
    }

    private YouTubePlayer.OnInitializedListener onInitializedListener() {
        return new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer,
                                                boolean b) {
                youTubePlayerScreen = youTubePlayer;
                youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener());
                playYoutubeVideo(youTubePlayer, videoUrl);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult initializationResult) {
                Log.d("test", "onInitializationFailure: ");

            }
        };
    }

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener() {
        return new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {

            }

            @Override
            public void onVideoEnded() {
                playYoutubeVideo(youTubePlayerScreen,
                        videoUrl);
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(youTubePlayerScreen != null) youTubePlayerScreen.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void playYoutubeVideo(YouTubePlayer youTubePlayer,
                                  String videoID) {
        youTubePlayer.loadVideo(videoID);
    }

    public void onPlayYoutubeVideo(String videoID) {
        playYoutubeVideo(youTubePlayerScreen, videoID);
    }

    public void onInitiazlizeVideo(int videoIndex) {
        youTubePlayerView.initialize(getApplicationContext().getString(com.tokopedia.core.R.string.GOOGLE_API_KEY),
                onInitializedListener());
    }

    @Override
    protected void onDestroy() {
        if(youTubePlayerScreen != null) {
            youTubePlayerScreen.release();
        }
        super.onDestroy();
    }
}