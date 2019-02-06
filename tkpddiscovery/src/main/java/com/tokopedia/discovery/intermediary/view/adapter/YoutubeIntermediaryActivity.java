package com.tokopedia.discovery.intermediary.view.adapter;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.tokopedia.discovery.R;

/**
 * Created by alifa on 5/26/17.
 */

public class YoutubeIntermediaryActivity extends YouTubeBaseActivity {

    public static final String EXTRA_YOUTUBE_VIDEO_URL = "EXTRA_YOUTUBE_VIDEO_URL";

    private YouTubePlayerView youTubePlayerView;


    private YouTubePlayer youTubePlayerScreen;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediary_youtube);
        getVideoDatas();
        initView();
        setSideBarAvailability();
    }

    private void initView() {
        youTubePlayerView = findViewById(R.id.youtube_player_main);
    }

    private void getVideoDatas() {
        videoUrl = getIntent().getStringExtra(EXTRA_YOUTUBE_VIDEO_URL);
    }

    private void setSideBarAvailability() {
        youTubePlayerView.initialize(getApplicationContext().getString(com.tokopedia.core2.R.string.GOOGLE_API_KEY),
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
        youTubePlayerView.initialize(getApplicationContext().getString(com.tokopedia.core2.R.string.GOOGLE_API_KEY),
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