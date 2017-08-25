package com.tokopedia.discovery.intermediary.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.intermediary.view.adapter.YoutubeIntermediaryActivity;

/**
 * Created by alifa on 5/26/17.
 */

public class YoutubeViewHolder extends RelativeLayout {

    private RelativeLayout mainView;
    private ProgressBar loadingBar;
    private String videoUrl;
    private YouTubeThumbnailLoader youTubeThumbnailLoader;
    private final String departmentId;

    public YoutubeViewHolder(Context context, String videoUrl, String departmentId) {
        super(context);
        this.videoUrl = videoUrl;
        this.departmentId = departmentId;
        initView(context, videoUrl);
    }

    public YoutubeViewHolder(Context context) {
        super(context);
    }

    public YoutubeViewHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context, final String youtubeVideoId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.video_youtube_placeholder, this, true);

        mainView = (RelativeLayout) findViewById(R.id.video_thumbnail_main_view);
        YouTubeThumbnailView youTubeThumbnailView = (YouTubeThumbnailView) findViewById(R.id.youtube_thumbnail_view);
        youTubeThumbnailView.setMinimumWidth(mainView.getWidth());
        loadingBar = (ProgressBar) findViewById(R.id.youtube_thumbnail_loading_bar);
        youTubeThumbnailView.initialize(getContext().getApplicationContext()
                        .getString(com.tokopedia.core.R.string.GOOGLE_API_KEY),
                thumbnailInitializedListener(youtubeVideoId));

        youTubeThumbnailView.setOnClickListener(onYoutubeThumbnailClickedListener());
    }

    private YouTubeThumbnailView
            .OnInitializedListener thumbnailInitializedListener(final String youtubeVideoId) {
        return new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
                                                final YouTubeThumbnailLoader loader) {
                youTubeThumbnailLoader = loader;
                loader.setVideo(youtubeVideoId);
                mainView.setVisibility(VISIBLE);
                loader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader
                        .OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView,
                                                  String s) {
                        loader.release();
                        loadingBar.setVisibility(GONE);
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView,
                                                 YouTubeThumbnailLoader.ErrorReason errorReason) {
                        loadingBar.setVisibility(GONE);

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeInitializationResult result) {
                loadingBar.setVisibility(GONE);
            }
        };
    }

    private OnClickListener onYoutubeThumbnailClickedListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: ask video name
                UnifyTracking.eventVideoIntermediary(departmentId,videoUrl);
                Intent intent = new Intent(getContext(), YoutubeIntermediaryActivity.class);
                intent.putExtra(YoutubeIntermediaryActivity.EXTRA_YOUTUBE_VIDEO_URL, videoUrl);
                getContext().startActivity(intent);
            }
        };
    }

    public void destroyReleaseProcess() {
        youTubeThumbnailLoader.release();
    }
}
