package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.blog;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.R2;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.BlogViewModel;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.SimpleMainThreadMediaPlayerListener;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

import butterknife.BindView;

/**
 * @author by nisie on 5/19/17.
 */

public class VideoBlogViewHolder extends AbstractViewHolder<BlogViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_blog_video;

    TextView title;
    TextView content;
    ImageView videoCover;
    VideoPlayerView video;
    private VideoPlayerManager<MetaData> videoPlayerManager;

    private final FeedPlus.View viewListener;

    public VideoBlogViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        content = (TextView) itemView.findViewById(R.id.content);
        videoCover = (ImageView) itemView.findViewById(R.id.video_cover);
        video = (VideoPlayerView) itemView.findViewById(R.id.video_player);

        videoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
            @Override
            public void onPlayerItemChanged(MetaData metaData) {

            }
        });

        this.viewListener = viewListener;
    }

    @Override
    public void bind(final BlogViewModel viewModel) {
        title.setText(MethodChecker.fromHtml(viewModel.getTitle()));
        content.setText(MethodChecker.fromHtml(viewModel.getContent()));

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToBlogWebView(viewModel.getUrl());
            }
        });

        ImageHandler.LoadImage(videoCover, viewModel.getImageUrl());

        if (!viewModel.getVideoUrl().equals("")
                && !viewModel.getImageUrl().equals("")) {
            videoCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    video.setVisibility(View.VISIBLE);
                    videoCover.setVisibility(View.GONE);
                    videoPlayerManager.playNewVideo(null, video, viewModel.getVideoUrl());
                }
            });

            video.addMediaPlayerListener(new SimpleMainThreadMediaPlayerListener() {
                @Override
                public void onVideoPreparedMainThread() {
                    videoCover.setVisibility(View.GONE);
                    video.setVisibility(View.VISIBLE);

                }

                @Override
                public void onVideoStoppedMainThread() {
                    videoCover.setVisibility(View.VISIBLE);
                    video.setVisibility(View.GONE);
                }

                @Override
                public void onVideoCompletionMainThread() {
                    videoCover.setVisibility(View.VISIBLE);
                    video.setVisibility(View.GONE);
                }
            });
        }
    }
}
