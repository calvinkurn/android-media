package com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;

/**
 * Created by stevenfredian on 5/31/17.
 */

public class RetryViewHolder extends AbstractViewHolder<RetryModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.retry_layout;
    private final View button;
    private final FeedPlus.View viewListener;

    public RetryViewHolder(View itemView, FeedPlus.View viewListener) {
        super(itemView);
        this.viewListener = viewListener;
        button = itemView.findViewById(R.id.retry);
    }

    @Override
    public void bind(RetryModel element) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewListener.onRetryClicked();
            }
        });
    }
}