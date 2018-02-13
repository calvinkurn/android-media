package com.tokopedia.home.beranda.presentation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.home.R;
import com.tokopedia.home.beranda.listener.HomeFeedListener;

/**
 * Created by henrypriyono on 1/12/18.
 */

public class RetryViewHolder extends AbstractViewHolder<RetryModel> {
    @LayoutRes
    public final static int LAYOUT = R.layout.retry_layout;
    private final View button;
    private final HomeFeedListener viewListener;

    public RetryViewHolder(View itemView, HomeFeedListener viewListener) {
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