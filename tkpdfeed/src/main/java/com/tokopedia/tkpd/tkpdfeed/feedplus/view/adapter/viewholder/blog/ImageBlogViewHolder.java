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

import butterknife.BindView;

/**
 * @author by nisie on 5/19/17.
 */

public class ImageBlogViewHolder extends AbstractViewHolder<BlogViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.list_feed_blog_image;

    private final FeedPlus.View viewListener;

    @BindView(R2.id.title)
    TextView title;

    @BindView(R2.id.content)
    TextView content;

    @BindView(R2.id.image)
    ImageView image;

    public ImageBlogViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
    }

    @Override
    public void bind(final BlogViewModel viewModel) {
        title.setText(MethodChecker.fromHtml(viewModel.getTitle()));
        content.setText(MethodChecker.fromHtml(viewModel.getContent()));
        ImageHandler.LoadImage(image, viewModel.getImageUrl());

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onGoToBlogWebView(viewModel.getUrl());
            }
        });
    }
}
