package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.blog;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.blog.BlogViewModel;

/**
 * @author by nisie on 5/19/17.
 */

public class VideoBlogViewHolder extends AbstractViewHolder<BlogViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_blog_video;

    TextView title;
    TextView content;
    ImageView videoCover;
    View shareButton;

    private final FeedPlus.View viewListener;

    public VideoBlogViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        content = (TextView) itemView.findViewById(R.id.content);
        videoCover = (ImageView) itemView.findViewById(R.id.video_cover);
        shareButton = itemView.findViewById(R.id.share_button);

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

        videoCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onOpenVideo(viewModel.getVideoUrl(),
                        viewModel.getTitle());
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onShareButtonClicked(
                        viewModel.getUrl(),
                        viewModel.getTitle(),
                        viewModel.getImageUrl(),
                        viewModel.getContent(),
                        viewModel.getPage() + "." + viewModel.getRowNumber());
            }
        });
    }
}
