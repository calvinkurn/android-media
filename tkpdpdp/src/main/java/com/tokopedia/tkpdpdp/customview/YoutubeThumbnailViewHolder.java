package com.tokopedia.tkpdpdp.customview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.tkpdpdp.ProductYoutubeActivity;
import com.tokopedia.tkpdpdp.R;

/**
 * @author kris on 11/3/16. Tokopedia
 */

public class YoutubeThumbnailViewHolder extends RelativeLayout{

    private YouTubeThumbnailLoadInProcess youTubeThumbnailInLoadProcess;
    private RelativeLayout mainView;
    private ProgressBar loadingBar;
    private VideoData videoData;
    private int selectedVideo;
    private YouTubeThumbnailLoader youTubeThumbnailLoader;

    public YoutubeThumbnailViewHolder(Context context, VideoData videoData, int selectedVideo,YouTubeThumbnailLoadInProcess youTubeThumbnailLoadInProcess) {
        super(context);
        this.videoData = videoData;
        this.selectedVideo = selectedVideo;
        initView(context, videoData.getVideo().get(selectedVideo).getUrl());
        this.youTubeThumbnailInLoadProcess = youTubeThumbnailLoadInProcess;
    }

    public YoutubeThumbnailViewHolder(Context context) {
        super(context);
    }

    public YoutubeThumbnailViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YoutubeThumbnailViewHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context, final String youtubeVideoId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.place_holder_youtube_view, this, true);

        mainView = (RelativeLayout) findViewById(R.id.video_thumbnail_main_view);
        YouTubeThumbnailView youTubeThumbnailView = (YouTubeThumbnailView) findViewById(R.id.youtube_thumbnail_view);
        loadingBar = (ProgressBar) findViewById(R.id.youtube_thumbnail_loading_bar);
        youTubeThumbnailView.initialize(getContext().getApplicationContext()
                .getString(R.string.GOOGLE_API_KEY),
                thumbnailInitializedListener(youtubeVideoId));
        if(youTubeThumbnailInLoadProcess != null)
            youTubeThumbnailInLoadProcess.onIntializationStart();


        //TODO Choose one of it
/*        youTubeThumbnailView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductYoutubeActivity.class);
                intent.putExtra(ProductYoutubeActivity.EXTRA_YOUTUBE_VIDEO_ID, youtubeVideoId);
                getContext().startActivity(intent);
            }
        });*/

        //TODO Choose one of it
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
                if(youTubeThumbnailInLoadProcess != null)
                    youTubeThumbnailInLoadProcess.onIntializationComplete();
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

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeInitializationResult result) {
                loadingBar.setVisibility(GONE);
                if(youTubeThumbnailInLoadProcess != null)
                    youTubeThumbnailInLoadProcess.onIntializationComplete();
            }
        };
    }

    private OnClickListener onYoutubeThumbnailClickedListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductYoutubeActivity.class);
                intent.putExtra(ProductYoutubeActivity.EXTRA_YOUTUBE_VIDEO_INDEX, selectedVideo);
                intent.putExtra(ProductYoutubeActivity.EXTRA_YOUTUBE_VIDEO_DATA, videoData);
                getContext().startActivity(intent);
            }
        };
    }

    public void destroyReleaseProcess() {
        youTubeThumbnailLoader.release();
    }

    public interface YouTubeThumbnailLoadInProcess {
        public void onIntializationStart();
        public void onIntializationComplete();
    }
}
